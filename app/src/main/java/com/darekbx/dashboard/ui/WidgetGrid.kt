package com.darekbx.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.darekbx.dashboard.ui.widgets.crypto.BitcoinPriceWidget
import com.darekbx.dashboard.ui.widgets.nbp.CurrencyWidget
import com.darekbx.dashboard.ui.widgets.nbp.GoldPriceWidget
import com.darekbx.dashboard.ui.widgets.stock.StockPriceWidget
import com.darekbx.dashboard.ui.widgets.waterlevel.WaterLevelWidget

@Composable
fun WidgetGrid(
    modifier: Modifier = Modifier,
    widgets: List<Widget>,
    columns: Int,
    rows: Int,
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
        columns = GridCells.Fixed(columns)
    ) {
        items(widgets) { widget ->
            val cellHeight = parentHeight
                ?.let { height -> with(LocalDensity.current) { (height / rows).toDp() } }
                ?: cellDefaultHeight

            when (widget) {
                is Currency -> CurrencyWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                is GoldPrice -> GoldPriceWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                is BitcoinPrice -> BitcoinPriceWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                is StockPrice -> StockPriceWidget(
                    modifier = modifier.height(cellHeight),
                    widget = widget
                )
                is WaterLevel -> WaterLevelWidget(
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
            BitcoinPrice(),
            StockPrice("PTON"),
            StockPrice("AUVI")
        )
        WidgetGrid(widgets = widgets, columns = 3, rows = 3, cellDefaultHeight = 200.dp)
    }
}