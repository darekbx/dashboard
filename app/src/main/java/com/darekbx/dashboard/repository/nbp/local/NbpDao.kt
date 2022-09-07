package com.darekbx.dashboard.repository.nbp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.dashboard.repository.nbp.local.entities.Currency
import com.darekbx.dashboard.repository.nbp.local.entities.GoldPrice

@Dao
interface NbpDao {

    @Query("SELECT * FROM currency WHERE `from` = :from AND `to` = :to")
    suspend fun listCurrencies(from: String, to: String): List<Currency>

    @Insert
    suspend fun add(currency: Currency)

    @Query("SELECT * FROM gold_price")
    suspend fun listGoldPrices(): List<GoldPrice>

    @Insert
    suspend fun add(goldPrice: GoldPrice)
}