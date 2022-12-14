package com.darekbx.dashboard.repository.imgw.remote.model

data class StationWrapper(
    val name: String,
    val state: String,
    val waterStateRecords: List<WaterStateRecord>) {
}

data class WaterStateRecord(
    val state: String,
    val value: Int,
    val date: String)