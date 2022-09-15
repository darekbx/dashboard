package com.darekbx.dashboard.ui.widgets.crypto

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.ui.common.ChartTitle
import com.darekbx.dashboard.ui.common.CommonWidgetCard
import com.darekbx.dashboard.ui.common.RefreshState
import com.darekbx.dashboard.ui.common.RefreshableContent
import com.darekbx.dashboard.viewmodel.SettingsViewModel

@Composable
fun BitcoinPriceWidget(
    modifier: Modifier = Modifier,
    cryptoViewModel: CryptoViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: BitcoinPrice
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CommonWrapper>?>(null) }

    RefreshableContent(settings.refreshInterval) { refreshState ->
        result = when (refreshState) {
            RefreshState.CLEAR -> null
            RefreshState.LOAD -> cryptoViewModel.fetchBitcoinPrice(widget)
        }
    }

    CommonWidgetCard(
        modifier,
        result,
        chartColor = Color(206,147,216),
        title = {
            result?.getOrNull()?.let {
                ChartTitle(name = "1 BTC", date = it.date)
            }
        }
    )
}
