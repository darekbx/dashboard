package com.darekbx.dashboard.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darekbx.dashboard.model.BitcoinPrice

@Composable
fun BitcoinPriceWidget(modifier: Modifier = Modifier, widget: BitcoinPrice) {
    Text(text = "Bitcoin price")
}