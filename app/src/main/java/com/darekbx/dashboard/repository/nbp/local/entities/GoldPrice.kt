package com.darekbx.dashboard.repository.nbp.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gold_price")
data class GoldPrice(
    @PrimaryKey(autoGenerate = true) val uid: Long?,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "date") val date: String
)