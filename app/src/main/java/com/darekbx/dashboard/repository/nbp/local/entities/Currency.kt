package com.darekbx.dashboard.repository.nbp.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = true) val uid: Long?,
    @ColumnInfo(name = "rate") val rate: Double,
    @ColumnInfo(name = "from") val from: String,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "date") val date: String
)