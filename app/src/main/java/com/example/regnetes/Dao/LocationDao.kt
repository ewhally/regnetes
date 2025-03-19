package com.example.regnetes.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.regnetes.entity.Location

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: Location)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): LiveData<List<Location>>
}