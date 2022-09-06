package com.darekbx.dashboard.repository.currency

import com.darekbx.dashboard.model.Currency

interface BaseCurrencyRepository {

    suspend fun fetchData(currency: Currency): CurrencyWrapper
}