package com.darekbx.dashboard.repository.nbp.remote.model

import com.google.gson.annotations.SerializedName

data class GoldPrice(
    @SerializedName("data")
    val date: String,
    @SerializedName("cena")
    val price: Double
)

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