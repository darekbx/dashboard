package com.darekbx.dashboard.repository.imgw.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_level")
data class WaterLevel(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "station_id") val stationId: Long,
)