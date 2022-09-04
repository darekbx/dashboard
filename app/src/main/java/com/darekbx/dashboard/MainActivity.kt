package com.darekbx.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.ui.WidgetGrid
import com.darekbx.dashboard.ui.theme.DashboardTheme

/**
 * Dashboard TODO
 *  - portrait and landscape
 *  - configurable grid (from settings)
 *  - generic widgets which can be choosed from the list
 *    - currencies (configurable, based on NBP)
 *    - gold price (static)
 *    - bitcoin curse (static)
 *    - stock prices (configurable)
 *    - river status (static, bulwary)
 *    - covid status (configurable country)
 *    - notepad (static, can be added few times, with title and edit icon, editing contents in dialog)
 *      - title: projects, contents: digital scale for water, screen time
 *
 *  - Settings
 *    - grid size 3x2, 4x3, ...
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val widgets = listOf(
                        Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.USD),
                        Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.EUR),
                        GoldPrice(),
                        BitcoinPrice()
                    )
                    WidgetGrid(columns = 2 /*TODO*/, widgets = widgets)
                }
            }
        }
    }
}

