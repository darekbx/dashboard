package com.darekbx.dashboard.repository.imgw.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.dashboard.repository.imgw.local.entities.WaterLevel

@Database(entities = [WaterLevel::class], version = 1)
abstract class WaterLevelDatabase : RoomDatabase() {

    abstract fun waterLevelDao(): WaterLevelDao
}