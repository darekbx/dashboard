package com.darekbx.dashboard.repository.nbp

data class CurrencyWrapper(
    val date: String = "",
    val rates: List<Float> = emptyList()
)

data class GoldPriceWrapper(
    val date: String = "",
    val goldPrices: List<Float> = emptyList()
)