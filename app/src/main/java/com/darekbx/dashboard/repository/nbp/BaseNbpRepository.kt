package com.darekbx.dashboard.repository.nbp

import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.CommonWrapper

interface BaseNbpRepository {

    suspend fun fetchCurrencyData(currency: Currency): Result<CommonWrapper>

    suspend fun fetchGoldPriceData(goldPrice: GoldPrice): Result<CommonWrapper>
}