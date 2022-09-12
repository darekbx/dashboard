package com.darekbx.dashboard.repository.crypto.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitcoin_price")
data class BitcoinPrice(
    @PrimaryKey(autoGenerate = true) val uid: Long?,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "date") val date: String
)