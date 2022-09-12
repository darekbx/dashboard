package com.darekbx.dashboard.repository.stockprice.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_price")
data class StockPrice(
    @PrimaryKey(autoGenerate = true) val uid: Long?,
    @ColumnInfo(name = "company_code") val companyCode: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "date") val date: String
)