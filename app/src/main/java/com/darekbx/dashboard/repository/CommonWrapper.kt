package com.darekbx.dashboard.repository

import java.text.SimpleDateFormat
import java.util.*

val commonDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

data class CommonWrapper(
    val date: String = "",
    var rates: List<Float> = listOf()
)