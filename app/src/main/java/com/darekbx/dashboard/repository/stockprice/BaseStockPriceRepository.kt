package com.darekbx.dashboard.repository.stockprice

import com.darekbx.dashboard.model.StockPrice
import com.darekbx.dashboard.repository.CommonWrapper

interface BaseStockPriceRepository {

    suspend fun fetchStockPrice(stockPrice: StockPrice): Result<CommonWrapper>
}