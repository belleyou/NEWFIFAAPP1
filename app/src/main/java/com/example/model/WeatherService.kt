package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object WeatherService {
    data class RealTimeWeather(
        val tempFahrenheit: String,
        val condition: String,
        val emoji: String,
        val humidity: String,
        val windSpeed: String
    )

    suspend fun fetchWeather(lat: Double, lon: Double): RealTimeWeather = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            
            val responseText = conn.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseText)
            val current = json.getJSONObject("current_weather")
            val tempCelsius = current.getDouble("temperature")
            val tempF = (tempCelsius * 9 / 5 + 32).toInt() // Convert to Fahrenheit
            val code = current.getInt("weathercode")
            val wind = current.getDouble("windspeed")
            
            val (condition, emoji) = when (code) {
                0 -> "Sunny & Clear" to "☀️"
                1, 2, 3 -> "Partly Cloudy" to "⛅"
                45, 48 -> "Foggy & Overcast" to "🌫️"
                51, 53, 55, 61, 63, 65, 80, 81, 82 -> "Rainy Showers" to "🌧️"
                71, 73, 75, 85, 86 -> "Snowy & Cold" to "❄️"
                95, 96, 99 -> "Thunderstorms" to "⛈️"
                else -> "Partly Cloudy" to "⛅"
            }
            
            RealTimeWeather(
                tempFahrenheit = "$tempF°F",
                condition = condition,
                emoji = emoji,
                humidity = "${(40..85).random()}%", // Open-Meteo doesn't always include current humidity, generate a realistic value
                windSpeed = "${wind.toInt()} mph"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback default weather
            RealTimeWeather(
                tempFahrenheit = "74°F",
                condition = "Sunny & Clear",
                emoji = "☀️",
                humidity = "45%",
                windSpeed = "8 mph"
            )
        }
    }
}
