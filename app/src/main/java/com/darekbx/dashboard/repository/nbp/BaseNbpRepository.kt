package com.darekbx.dashboard.repository.nbp

import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice

interface BaseNbpRepository {

    suspend fun fetchCurrencyData(currency: Currency): Result<CurrencyWrapper>

    suspend fun fetchGoldPriceData(goldPrice: GoldPrice): Result<GoldPriceWrapper>
}