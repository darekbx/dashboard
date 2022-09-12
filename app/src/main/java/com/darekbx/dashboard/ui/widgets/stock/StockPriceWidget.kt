package com.darekbx.dashboard.ui.widgets.stock

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
import com.darekbx.dashboard.model.StockPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.ui.common.CommonWidgetCard
import com.darekbx.dashboard.ui.common.RefreshState
import com.darekbx.dashboard.ui.common.RefreshableContent
import com.darekbx.dashboard.viewmodel.SettingsViewModel

@Composable
fun StockPriceWidget(
    modifier: Modifier = Modifier,
    stockPriceViewModel: StockPriceViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: StockPrice
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CommonWrapper>?>(null) }

    RefreshableContent(settings.refreshInterval) { refreshState ->
        result = when (refreshState) {
            RefreshState.CLEAR -> null
            RefreshState.LOAD -> stockPriceViewModel.fetchStockPrice(widget)
        }
    }

    CommonWidgetCard(
        modifier,
        result,
        chartColor = computeChartColor(result?.getOrNull()),
        title = {
            result?.getOrNull()?.let {
                StockPriceTitle(Modifier, widget = widget, date = it.date)
            }
        }
    )
}

@Composable
private fun computeChartColor(data: CommonWrapper?): Color {
    val dataCount = data?.rates?.size
        ?: return Color.White
    return when {
        dataCount > 1 -> {
            val last = data.rates[dataCount - 1]
            val penultimate = data.rates[dataCount - 2]
            when {
                penultimate > last -> Color.Red
                penultimate < last -> Color.Green
                else -> Color.White
            }
        }
        else -> Color.White
    }
}

@Composable
private fun StockPriceTitle(
    modifier: Modifier = Modifier,
    widget: StockPrice,
    date: String
) {
    val text = buildAnnotatedString {
        append("${widget.companyCode} ")
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
