package com.darekbx.dashboard.repository.nbp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.dashboard.repository.nbp.local.entities.Currency
import com.darekbx.dashboard.repository.nbp.local.entities.GoldPrice

@Database(entities = [Currency::class, GoldPrice::class], version = 1)
abstract class NbpDatabase : RoomDatabase() {

    abstract fun nbpDao(): NbpDao
}
