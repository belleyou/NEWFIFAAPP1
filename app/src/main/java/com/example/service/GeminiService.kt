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
}
