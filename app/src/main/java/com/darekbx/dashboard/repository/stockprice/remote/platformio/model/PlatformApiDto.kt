package com.darekbx.dashboard.repository.stockprice.remote.platformio.model

import com.google.gson.annotations.SerializedName

data class StockPrice(
    val results: List<Result>
)

data class Result(
    @SerializedName("c")
    val closePrice: Double,
    @SerializedName("t")
    val timestamp: Long
)