package com.darekbx.dashboard.ui.widgets.waterlevel

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.WaterLevel
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.imgw.remote.BaseWaterLevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaterLevelViewModel @Inject constructor(
    private val waterLevelRepository: BaseWaterLevelRepository
) : ViewModel() {

    suspend fun fetchStationInfo(status: WaterLevel): Result<CommonWrapper> {
        return waterLevelRepository.fetchStationInfo(status)
    }
}
