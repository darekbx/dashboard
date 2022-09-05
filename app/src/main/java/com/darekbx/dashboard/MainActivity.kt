package com.darekbx.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.ui.WidgetGrid
import com.darekbx.dashboard.ui.theme.DashboardTheme
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
                        BitcoinPrice()
                    )
                    WidgetGrid(columns = 2 /*TODO*/, widgets = widgets)
                }
            }
        }
    }
}

