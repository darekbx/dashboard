package com.darekbx.dashboard.repository.currency

import com.darekbx.dashboard.model.Currency
import java.lang.IllegalStateException
import kotlin.random.Random

class CurrencyRepository {

    private val mockDataUsd = mutableListOf(4.65F)
    private val mockDataEur = mutableListOf(4.45F)

    suspend fun fetchData(currency: Currency): List<Float> {
        if (currency.to == Currency.CurrencyType.USD) {
            mockDataUsd.add(Random.nextFloat() + 4.6F)
            return mockDataUsd
        } else if (currency.to == Currency.CurrencyType.EUR) {
            mockDataEur.add(Random.nextFloat() + 4.5F)
            return mockDataEur
        } else {
            throw IllegalStateException("Unknown")
        }
    }
}