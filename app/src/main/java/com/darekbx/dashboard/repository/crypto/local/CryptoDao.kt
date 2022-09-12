package com.darekbx.dashboard.repository.crypto.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.dashboard.repository.crypto.local.entities.BitcoinPrice

@Dao
interface CryptoDao {

    @Query("SELECT * FROM bitcoin_price WHERE price > 0")
    suspend fun listBitcoinPrices(): List<BitcoinPrice>

    @Insert
    suspend fun add(bitcoinPrice: BitcoinPrice)

    @Query("DELETE FROM bitcoin_price")
    suspend fun deleteAll()

    @Query("DELETE FROM bitcoin_price WHERE uid = :id")
    suspend fun delete(id: Long)
}