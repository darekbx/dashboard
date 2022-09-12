package com.darekbx.dashboard.ui.widgets.crypto

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.repository.CommonWrapper
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
                BitcoinPriceTitle(date = it.date)
            }
        }
    )
}

@Composable
private fun BitcoinPriceTitle(
    modifier: Modifier = Modifier,
    date: String
) {
    val text = buildAnnotatedString {
        append("1 BTC")
        withStyle(
            SpanStyle(
            fontSize = 8.sp,
            color = Color.LightGray,
            // Used to center date info
            baselineShift = BaselineShift(0.11F)
        )
        ) {
            append(" ($date)")
        }
    }
    Text(
        modifier = modifier.padding(2.dp),
        text = text,
        style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    )
}
