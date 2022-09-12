package com.darekbx.dashboard.repository.stockprice.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.dashboard.repository.stockprice.local.entities.StockPrice

@Database(entities = [StockPrice::class], version = 1)
abstract class StockPriceDatabase : RoomDatabase() {

    abstract fun stockPriceDao(): StockPriceDao
}
