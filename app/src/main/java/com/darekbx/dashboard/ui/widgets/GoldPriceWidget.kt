package com.darekbx.dashboard.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darekbx.dashboard.model.GoldPrice

@Composable
fun GoldPriceWidget(modifier: Modifier = Modifier, widget: GoldPrice) {
    Text(text = "Gold price")
}