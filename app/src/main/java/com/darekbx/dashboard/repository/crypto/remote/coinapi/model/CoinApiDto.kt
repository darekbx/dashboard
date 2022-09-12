package com.darekbx.dashboard.repository.crypto.remote.coinapi.model

import com.google.gson.annotations.SerializedName

data class Trade(
    val price: Double,
    @SerializedName("time_exchange")
    val timeExchange: String
)