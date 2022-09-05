package com.darekbx.dashboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.darekbx.dashboard.model.*
import com.darekbx.dashboard.ui.theme.DashboardTheme
import com.darekbx.dashboard.ui.widgets.BitcoinPriceWidget
import com.darekbx.dashboard.ui.widgets.CurrencyWidget
import com.darekbx.dashboard.ui.widgets.GoldPriceWidget

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WidgetGrid(
    modifier: Modifier = Modifier,
    widgets: List<Widget>,
    columns: Int,
    cellDefaultHeight: Dp = 0.dp,
) {
    var target by remember {
        mutableStateOf<LayoutCoordinates?>(null)
    }
    val parentHeight = target?.boundsInParent()?.height

    LazyVerticalGrid(
        modifier = modifier
            .background(Color.Black)
            .onGloballyPositioned { target = it },
        cells = GridCells.Fixed(columns)
    ) {
        items(widgets) { widget ->
            val cellHeight = parentHeight
                ?.let { height -> with(LocalDensity.current) { (height / 2).toDp() } }
                ?: cellDefaultHeight

            when (widget) {
                is Currency -> CurrencyWidget(
                    modifier = modifier.height(cellHeight),
                    currency = widget
                )
                is GoldPrice -> GoldPriceWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                is BitcoinPrice -> BitcoinPriceWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun WidgetGridPreview() {
    DashboardTheme {
        val widgets = listOf(
            Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.USD),
            Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.EUR),
            GoldPrice(),
            BitcoinPrice()
        )
        WidgetGrid(widgets = widgets, columns = 3, cellDefaultHeight = 200.dp)
    }
}