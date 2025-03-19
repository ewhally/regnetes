package com.example.regnetes

import android.app.Application
import androidx.lifecycle.*
import com.example.regnetes.entity.Location
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val locationDao = AppDatabase.getDatabase(application).locationDao()

    val allLocations: LiveData<List<Location>> = locationDao.getAllLocations()

    fun insertLocation(location: Location) {
        viewModelScope.launch {
            locationDao.insert(location)
        }
    }
}
