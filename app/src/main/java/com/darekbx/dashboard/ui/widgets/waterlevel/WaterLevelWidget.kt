package com.darekbx.dashboard.ui.widgets.waterlevel

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.WaterLevel
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.ui.common.ChartTitle
import com.darekbx.dashboard.ui.common.CommonWidgetCard
import com.darekbx.dashboard.ui.common.RefreshState
import com.darekbx.dashboard.ui.common.RefreshableContent
import com.darekbx.dashboard.viewmodel.SettingsViewModel

@Composable
fun WaterLevelWidget(
    modifier: Modifier = Modifier,
    waterLevelViewModel: WaterLevelViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: WaterLevel
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CommonWrapper>?>(null) }

    RefreshableContent(settings.refreshInterval) { refreshState ->
        result = when (refreshState) {
            RefreshState.CLEAR -> null
            RefreshState.LOAD -> waterLevelViewModel.fetchStationInfo(widget)
        }
    }

    CommonWidgetCard(
        modifier,
        result,
        chartColor = Color(21, 101, 192),
        title = {
            result?.getOrNull()?.let {
                ChartTitle(name = "${widget.stationName} ", date = it.date)
            }
        }
    )
}
