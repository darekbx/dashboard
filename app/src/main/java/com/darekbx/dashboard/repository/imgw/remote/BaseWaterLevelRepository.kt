package com.darekbx.dashboard.repository.imgw.remote

import com.darekbx.dashboard.model.WaterLevel
import com.darekbx.dashboard.repository.CommonWrapper

interface BaseWaterLevelRepository {

    suspend fun fetchStationInfo(waterLevel: WaterLevel): Result<CommonWrapper>
}