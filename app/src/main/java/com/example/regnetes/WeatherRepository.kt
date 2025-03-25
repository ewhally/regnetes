package com.example.regnetes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class WeatherRepository {
    suspend fun fetchWeatherForLocation(plzPrefix: String): Double? {
        val url = URL("https://app-prod-ws.meteoswiss-app.ch/v1/plzDetail?plz=${plzPrefix}00")
        val connection = url.openConnection() as HttpURLConnection
        return withContext(Dispatchers.IO) {
            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("User-Agent", "RegnetesApp")

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(responseText)
                    val forecastArray = jsonResponse.getJSONArray("forecast")
                    if (forecastArray.length() > 0) {
                        val todayForecast = forecastArray.getJSONObject(0)
                        return@withContext todayForecast.getDouble("precipitation")
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                connection.disconnect()
            }
        }
    }
}
