package com.example.regnetes

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking

class WeatherCheckWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val weatherRepository = WeatherRepository()
        val context = applicationContext
        val locationDao = AppDatabase.getDatabase(context).locationDao()
            runBlocking {
                val currentLocations = locationDao.getAllLocations().value ?: emptyList()
                val rainLocations = mutableListOf<String>()

                for (location in currentLocations) {
                    val precipitation = weatherRepository.fetchWeatherForLocation(location.name.trim()) ?: 0.0
                    if (precipitation > 2.5) {
                        rainLocations.add(location.name)
                    }
                }

                if (rainLocations.isNotEmpty()) {
                    val message = rainLocations.joinToString(", ") { "$it is raining today" }
                    NotificationHelper.showNotification(applicationContext, "Weather Alert", message)
                }
            }


        return Result.success()
    }
}
