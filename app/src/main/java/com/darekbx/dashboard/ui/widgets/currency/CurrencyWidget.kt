package com.darekbx.dashboard.ui.widgets

import android.graphics.PointF
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.ui.common.Progress
import com.darekbx.dashboard.ui.widgets.currency.CurrencyViewModel
import com.darekbx.dashboard.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun CurrencyWidget(
    modifier: Modifier = Modifier,
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    currency: Currency,
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var currencyData by remember { mutableStateOf(emptyList<Float>()) }
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(currency) {
        do {
            // `toMutableList` is used only to crate new reference of the list.
            // New reference is needed to invoke recomposition.
            currencyData = currencyViewModel.fetchCurrencyData(currency).toMutableList()
            delay(settings.refreshInterval)
        } while (isActive)
    }

    DisposableEffect(currency) {
        onDispose {
            isActive = false
        }
    }
    
    CurrencyWidgetCard(modifier, currencyData, currency)
}

@Composable
private fun CurrencyWidgetCard(
    modifier: Modifier = Modifier,
    currencyData: List<Float>?,
    widget: Currency
) {
    Card(
        modifier
            .padding(8.dp),
        elevation = 8.dp,
        border = BorderStroke(1.dp,Color(33, 35, 39)),
        shape = MaterialTheme.shapes.small.copy(all = CornerSize(16.dp)),
        backgroundColor = Color(27, 29, 33)
    ) {
        if (currencyData == null) {
            Progress()
        } else {
            Column(
                Modifier.padding(4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Chart(
                    Modifier
                        .fillMaxSize()
                        .weight(1f), currencyData = currencyData
                )
                Title(Modifier, widget = widget)
            }
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    widget: Currency
) {
    val text = buildAnnotatedString {
        append("${widget.from}")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(" to ")
        }
        append("${widget.to}")
    }
    Text(
        modifier = modifier.padding(2.dp),
        text = text,
        style = TextStyle(fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
    )
}

@Composable
private fun Chart(
    modifier: Modifier = Modifier,
    currencyData: List<Float>
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(Color.White.copy(alpha = 0.05F), shape = RoundedCornerShape(4.dp))
    ) {
        ChartDescription(currencyData)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 4.dp, end = 4.dp, bottom = 8.dp),
            onDraw = {
                val count = currencyData.size
                val minValue = currencyData.minOrNull() ?: return@Canvas
                val maxValue = currencyData.maxOrNull() ?: return@Canvas
                val widthRatio = size.width / (count - 1)
                val heightRatio = size.height / (maxValue - minValue)
                val firstPoint = PointF(
                    0F,
                    size.height - ((currencyData.first() - minValue) * heightRatio)
                )

                currencyData.forEachIndexed { index, value ->
                    if (index == 0) {
                        return@forEachIndexed
                    }

                    val x = (index * widthRatio)
                    val y = size.height - ((value - minValue) * heightRatio)

                    drawLine(
                        Color.White,
                        Offset(firstPoint.x, firstPoint.y),
                        Offset(x, y)
                    )

                    firstPoint.x = x
                    firstPoint.y = y
                }
            }
        )
    }
}

@Composable
private fun ChartDescription(currencyData: List<Float>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 8.dp),
        onDraw = {

            val minValue = currencyData.minOrNull() ?: return@Canvas
            val maxValue = currencyData.maxOrNull() ?: return@Canvas
            val latest = currencyData.last()
            val heightRatio = size.height / (maxValue - minValue)

            val leftPosition = 16.dp.toPx()
            val latestDisplayTrashold = 0.05F
            val showLatest = abs(latest - maxValue) > latestDisplayTrashold ||
                    abs(latest - minValue) > latestDisplayTrashold

            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
            val paint = Paint().asFrameworkPaint().apply {
                color = android.graphics.Color.LTGRAY
                textSize = 18F
            }

            drawIntoCanvas {
                with(it.nativeCanvas) {
                    drawText("%.2f".format(maxValue), 0F, 8F, paint)
                    val yPos = (maxValue - minValue) * heightRatio
                    drawText("%.2f".format(minValue), 0F, yPos + 8F, paint)
                    if (showLatest) {
                        val yPosLatest = (maxValue - latest) * heightRatio
                        drawText("%.2f".format(latest), 0F, yPosLatest + 8F, paint)
                    }
                }
            }

            // Maximum line
            drawLine(
                Color.Gray,
                Offset(leftPosition, 0F),
                Offset(size.width, 0F),
                pathEffect = pathEffect
            )
            // Minimum line
            drawLine(
                Color.Gray,
                Offset(leftPosition, (maxValue - minValue) * heightRatio),
                Offset(size.width, (maxValue - minValue) * heightRatio),
                pathEffect = pathEffect
            )
            if (showLatest) {
                // Latest line
                drawLine(
                    Color.Gray,
                    Offset(leftPosition, (maxValue - latest) * heightRatio),
                    Offset(size.width, (maxValue - latest) * heightRatio),
                    pathEffect = pathEffect
                )
            }
        })
}

@Preview
@Composable
fun CurrencyWidgetPreview() {
    CurrencyWidgetCard(
        Modifier.size(250.dp, 200.dp),
        listOf(4.56f, 4.57f, 4.60f, 4.59f, 4.53f, 4.61f, 4.64f, 4.69f, 4.68f, 4.65f, 4.60f, 4.56f),
        Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.USD)
    )
}