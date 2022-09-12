package com.darekbx.dashboard.repository.crypto.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.dashboard.repository.crypto.local.entities.BitcoinPrice

@Database(entities = [BitcoinPrice::class], version = 1)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun cryptoDao(): CryptoDao
}
