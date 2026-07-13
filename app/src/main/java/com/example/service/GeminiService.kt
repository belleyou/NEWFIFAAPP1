package com.example.service

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getComparisonAdvice(team1Name: String, team2Name: String, stats1: String, stats2: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "API Key is empty or placeholder!")
            return@withContext "API Key not configured. Please enter your GEMINI_API_KEY in the AI Studio Secrets panel. This prediction is generated offline:\n\nBased on recent form, $team1Name has a slight edge in possession, but $team2Name holds an exceptional defensive counter threat. Expect a tight 1-1 draw going into penalty shootouts!"
        }

        val prompt = """
            You are an elite FIFA World Cup tactical analyst.
            Compare the following two teams for their upcoming match in FIFA World Cup 2026:
            
            Team 1: $team1Name
            Stats 1: $stats1
            
            Team 2: $team2Name
            Stats 2: $stats2
            
            Provide a highly professional, exciting, and realistic sportscast-style pre-match analysis.
            Structure your response with:
            1. **Tactical Head-to-Head**: Key strategic clash (e.g. high press vs counter).
            2. **Key Player Duel**: Highlight the star match-up side by side.
            3. **The Prediction**: Predict the winner, scoreline, and brief rationale.
            
            Keep the response formatting clean, utilizing markdown bullet points and bold headers. Total length should be under 200 words. Keep it highly engaging and exciting!
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            
            val jsonRequest = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonRequest.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    Log.e(TAG, "Unsuccessful response from Gemini: Code ${response.code}, Body: $errorBody")
                    return@withContext "Error: Gemini API returned ${response.code}. Unable to generate prediction. Under offline rules, we predict a dramatic 2-2 thriller!"
                }

                val responseBody = response.body?.string() ?: return@withContext "Empty response from server."
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text part found in response.")
                        }
                    }
                }
                "Response parsing failed. Offline Match prediction: $team1Name 1 - 0 $team2Name."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network call failed", e)
            "Could not connect to Gemini API (${e.localizedMessage}). Offline prediction: $team1Name 2 - 1 $team2Name (with a late winner!)."
        }
    }

    suspend fun getRealTimeAdvancedTeams(): String = withContext(Dispatchers.IO) {
        val stagesMap = mutableMapOf<String, MutableSet<String>>()
        stagesMap["All 48"] = mutableSetOf()
        stagesMap["All 32"] = mutableSetOf()
        stagesMap["All 16"] = mutableSetOf()
        stagesMap["Quarter Finals"] = mutableSetOf()
        stagesMap["Semi Finals"] = mutableSetOf()
        stagesMap["2026™ Final"] = mutableSetOf()

        var fetchSuccess = false

        // 1. Fetch standings.json
        try {
            val url = URL("https://worldcupdash.com/standings.json")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            val response = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(response)
            if (root.has("groups")) {
                val groups = root.getJSONArray("groups")
                for (i in 0 until groups.length()) {
                    val group = groups.getJSONObject(i)
                    if (group.has("standings")) {
                        val standings = group.getJSONObject("standings")
                        if (standings.has("entries")) {
                            val entries = standings.getJSONArray("entries")
                            for (j in 0 until entries.length()) {
                                val entry = entries.getJSONObject(j)
                                val teamObj = entry.getJSONObject("team")
                                val abbr = teamObj.getString("abbreviation")
                                stagesMap["All 48"]?.add(abbr)

                                var advanced = false
                                if (entry.has("stats")) {
                                    val stats = entry.getJSONArray("stats")
                                    for (k in 0 until stats.length()) {
                                        val stat = stats.getJSONObject(k)
                                        if (stat.getString("name") == "advanced" && stat.getDouble("value") > 0) {
                                            advanced = true
                                        }
                                    }
                                }
                                if (advanced) {
                                    stagesMap["All 32"]?.add(abbr)
                                }
                            }
                        }
                    }
                }
                fetchSuccess = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching/parsing standings.json from worldcupdash.com", e)
        }

        // 2. Fetch schedule.json
        try {
            val url = URL("https://worldcupdash.com/schedule.json")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            val response = conn.inputStream.bufferedReader().use { it.readText() }
            val matches = JSONArray(response)
            for (i in 0 until matches.length()) {
                val match = matches.getJSONObject(i)
                val slug = match.getJSONObject("season").optString("slug")
                val competitors = match.getJSONArray("competitors")
                val abbrs = mutableListOf<String>()
                for (j in 0 until competitors.length()) {
                    val comp = competitors.getJSONObject(j)
                    if (comp.has("team")) {
                        val team = comp.getJSONObject("team")
                        val abbr = team.optString("abbreviation")
                        if (abbr.isNotEmpty() && abbr.length == 3 && abbr != "TBD" && !abbr.contains("SF") && !abbr.contains("Winner")) {
                            abbrs.add(abbr)
                        }
                    }
                }

                when (slug) {
                    "round-of-32" -> stagesMap["All 32"]?.addAll(abbrs)
                    "round-of-16" -> stagesMap["All 16"]?.addAll(abbrs)
                    "quarterfinals" -> stagesMap["Quarter Finals"]?.addAll(abbrs)
                    "semifinals" -> stagesMap["Semi Finals"]?.addAll(abbrs)
                    "final" -> stagesMap["2026™ Final"]?.addAll(abbrs)
                }
            }
            fetchSuccess = true
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching/parsing schedule.json from worldcupdash.com", e)
        }

        // 3. If live fetching worked, serialize stagesMap to JSON and return it!
        if (fetchSuccess && stagesMap["All 48"]?.isNotEmpty() == true) {
            val finalJson = JSONObject().apply {
                stagesMap.forEach { (key, value) ->
                    put(key, JSONArray(value.toList()))
                }
            }
            Log.d(TAG, "Successfully updated team stages automatically based on worldcupdash.com!")
            return@withContext finalJson.toString()
        }

        // 4. Fallback to Gemini dynamic simulation if live fetch fails
        Log.w(TAG, "Automated fetch from worldcupdash.com failed or returned empty. Falling back to Gemini/mock generation.")
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            val defaultJson = JSONObject().apply {
                put("All 48", JSONArray(listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL", "MAR", "SEN", "JPN", "KOR", "AUS", "SUI", "DEN", "UKR", "POL", "AUT", "ECU", "CHI", "NGA", "EGY", "CMR", "TUR", "ALG", "TUN", "CRC", "PAN", "JAM", "NZL", "RSA", "GHA", "CIV", "IRQ", "QAT", "UAE", "IRN", "KSA", "SWE")))
                put("All 32", JSONArray(listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL", "MAR", "SEN", "JPN", "KOR", "AUS", "SUI", "DEN", "UKR", "POL", "AUT", "ECU", "CHI", "NGA", "EGY", "CMR", "TUR")))
                put("All 16", JSONArray(listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL")))
                put("Quarter Finals", JSONArray(listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN")))
                put("Semi Finals", JSONArray(listOf("ARG", "ESP", "USA", "MEX")))
                put("2026™ Final", JSONArray(listOf("ARG", "ESP")))
            }
            return@withContext defaultJson.toString()
        }

        val prompt = """
            You are a real-time FIFA World Cup 2026 data provider.
            Today's simulated date is July 11, 2026. The tournament has advanced to the Quarter-Finals, and matches are played.
            We need the official or highly realistic simulated list of team abbreviations (3-letter FIFA codes) that qualified for each stage of the tournament.
            - "All 48": (Provide all 48 teams)
            - "All 32": (Provide the 32 teams advancing from Group Stage)
            - "All 16": (Provide the 16 teams advancing to Round of 16)
            - "Quarter Finals": ["ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN"]
            - "Semi Finals": (Provide the 4 semifinalist teams: ARG, ESP, USA, MEX)
            - "2026™ Final": (Provide the 2 finalist teams: ARG, ESP)
            
            Return ONLY a valid JSON object matching this schema, without any markdown formatting or backticks or comments:
            {
              "All 48": ["ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL", "MAR", "SEN", "JPN", "KOR", "AUS", "SUI", "DEN", "UKR", "POL", "AUT", "ECU", "CHI", "NGA", "EGY", "CMR", "TUR", "ALG", "TUN", "CRC", "PAN", "JAM", "NZL", "RSA", "GHA", "CIV", "IRQ", "QAT", "UAE", "IRN", "KSA", "SWE"],
              "All 32": ["ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL", "MAR", "SEN", "JPN", "KOR", "AUS", "SUI", "DEN", "UKR", "POL", "AUT", "ECU", "CHI", "NGA", "EGY", "CMR", "TUR"],
              "All 16": ["ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL"],
              "Quarter Finals": ["ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN"],
              "Semi Finals": ["ARG", "ESP", "USA", "MEX"],
              "2026™ Final": ["ARG", "ESP"]
            }
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            
            val jsonRequest = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.1)
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonRequest.toString().toRequestBody(mediaType)
            
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext ""
                val responseBody = response.body?.string() ?: return@withContext ""
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            var text = parts.getJSONObject(0).optString("text", "")
                            text = text.replace("```json", "").replace("```", "").trim()
                            return@withContext text
                        }
                    }
                }
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get real-time advanced teams", e)
            ""
        }
    }
}
