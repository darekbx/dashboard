package com.darekbx.dashboard.repository.currency

data class CurrencyWrapper(
    val date: String = "",
    val rates: List<Float> = emptyList(),
    val errorMessage: String? = null
) {

    val hasError = errorMessage != null
}