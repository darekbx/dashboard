package com.darekbx.dashboard.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WidgetCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier
            .padding(8.dp),
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color(33, 35, 39)),
        shape = MaterialTheme.shapes.small.copy(all = CornerSize(16.dp)),
        backgroundColor = Color(27, 29, 33)
    ) {
        content()
    }
}
