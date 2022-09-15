package com.darekbx.dashboard.repository.imgw.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.dashboard.repository.imgw.local.entities.WaterLevel

@Dao
interface WaterLevelDao {

    @Query("SELECT * FROM water_level ORDER BY date DESC")
    suspend fun fetch(): List<WaterLevel>

    @Query("SELECT * FROM water_level ORDER BY date DESC LIMIT 1")
    suspend fun fetchLast(): WaterLevel?

    @Insert
    suspend fun insert(warterLevelDtos: List<WaterLevel>)
}
