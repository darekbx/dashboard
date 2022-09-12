package com.darekbx.dashboard.ui.widgets.nbp

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.ui.common.CommonWidgetCard
import com.darekbx.dashboard.ui.common.RefreshState
import com.darekbx.dashboard.ui.common.RefreshableContent
import com.darekbx.dashboard.viewmodel.SettingsViewModel

@Composable
fun GoldPriceWidget(
    modifier: Modifier = Modifier,
    nbpViewModel: NbpViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: GoldPrice,
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CommonWrapper>?>(null) }

    RefreshableContent(settings.refreshInterval) { refreshState ->
        result = when (refreshState) {
            RefreshState.CLEAR -> null
            RefreshState.LOAD -> nbpViewModel.fetchGoldPriceData(widget)
        }
    }

    CommonWidgetCard(
        modifier,
        result,
        chartColor = Color(255, 215, 0),
        title = {
            result?.getOrNull()?.let {
                GoldPriceTitle(Modifier, date = it.date)
            }
        }
    )
}

@Composable
fun CurrencyWidget(
    modifier: Modifier = Modifier,
    nbpViewModel: NbpViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: Currency,
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CommonWrapper>?>(null) }

    RefreshableContent(settings.refreshInterval) { refreshState ->
        result = when (refreshState) {
            RefreshState.CLEAR -> null
            RefreshState.LOAD -> nbpViewModel.fetchCurrencyData(widget)
        }
    }

    val chartColor = when (widget.to) {
        Currency.CurrencyType.USD -> Color(77, 182, 172)
        Currency.CurrencyType.EUR -> Color(100, 181, 246)
        else -> Color.White
    }
    CommonWidgetCard(
        modifier,
        result,
        chartColor = chartColor,
        title = {
            result?.getOrNull()?.let {
                CurrencyTitle(Modifier, widget = widget, date = it.date)
            }
        }
    )
}

@Composable
private fun CurrencyTitle(
    modifier: Modifier = Modifier,
    widget: Currency,
    date: String
) {
    val text = buildAnnotatedString {
        append("${widget.from}")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(" to ")
        }
        append("${widget.to}")
        withStyle(SpanStyle(
            fontSize = 8.sp,
            color = Color.LightGray,
            // Used to center date info
            baselineShift = BaselineShift(0.11F)
        )) {
            append(" ($date)")
        }
    }
    Text(
        modifier = modifier.padding(2.dp),
        text = text,
        style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    )
}

@Composable
private fun GoldPriceTitle(
    modifier: Modifier = Modifier,
    date: String
) {
    val text = buildAnnotatedString {
        append("1 oz of gold")
        withStyle(SpanStyle(
            fontSize = 8.sp,
            color = Color.LightGray,
            // Used to center date info
            baselineShift = BaselineShift(0.11F)
        )) {
            append(" ($date)")
        }
    }
    Text(
        modifier = modifier.padding(2.dp),
        text = text,
        style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    )
}

@Preview
@Composable
fun GoldPriceWidgetPreview() {
    CommonWidgetCard(
        Modifier.size(250.dp, 200.dp),
        Result.success(
            CommonWrapper(
                "2022-09-06",
                listOf(7372F, 7370F, 7400F, 7380F, 7390F, 7385F, 7395F, 7390F)
            )
        ),
        Color.LightGray,
    ) { GoldPriceTitle(date = "2022-12-22") }
}
