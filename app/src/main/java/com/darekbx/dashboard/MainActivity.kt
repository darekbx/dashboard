package com.darekbx.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.darekbx.dashboard.model.*
import com.darekbx.dashboard.ui.WidgetGrid
import com.darekbx.dashboard.ui.theme.DashboardTheme
import com.darekbx.dashboard.ui.widgets.waterlevel.WaterLevelWidget
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dashboard TODO
 *  - portrait and landscape
 *  - configurable grid (from settings, use DataStore)
 *  - generic widgets which can be choosed from the list
 *    - currencies (configurable, based on NBP, only from PLN in settings screen)
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

@AndroidEntryPoint
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
                        BitcoinPrice(),
                        StockPrice("PTON"),
                        StockPrice("AUVI"),
                        WaterLevel(152210170, "Warszawa Bulwary")
                    )
                    WidgetGrid(columns = 3 /*TODO*/, rows = 3, widgets = widgets)
                }
            }
        }
    }
}

