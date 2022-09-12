package com.darekbx.dashboard.repository

data class CommonWrapper(
    val date: String = "",
    var rates: List<Float> = listOf()
)