package com.darekbx.dashboard.repository.currency.nbp.model

import com.google.gson.annotations.SerializedName

data class RatesWrapper(
    @SerializedName("effectiveDate")
    val date: String,
    val rates: List<Rate>
)

data class Rate(
    @SerializedName("currency")
    val currencyName: String,
    val code: String,
    @SerializedName("mid")
    val value: Double
)