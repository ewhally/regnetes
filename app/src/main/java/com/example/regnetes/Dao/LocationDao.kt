package com.example.regnetes.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.regnetes.entity.Location

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: Location)

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): LiveData<List<Location>>
}