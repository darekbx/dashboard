package com.darekbx.dashboard.repository.stockprice.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.dashboard.repository.stockprice.local.entities.StockPrice

@Dao
interface StockPriceDao {

    @Query("SELECT * FROM stock_price WHERE company_code = :companyCode")
    suspend fun listStockPrices(companyCode: String): List<StockPrice>

    @Insert
    suspend fun add(stockPrice: StockPrice)

    @Query("DELETE FROM stock_price WHERE uid = :id")
    suspend fun delete(id: Long)
}