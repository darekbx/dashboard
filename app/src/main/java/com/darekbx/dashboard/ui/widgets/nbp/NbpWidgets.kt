package com.darekbx.dashboard.ui.widgets.nbp

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
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.nbp.CurrencyWrapper
import com.darekbx.dashboard.repository.nbp.GoldPriceWrapper
import com.darekbx.dashboard.ui.common.Progress
import com.darekbx.dashboard.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import java.lang.IllegalStateException
import kotlin.math.abs

@Composable
fun GoldPriceWidget(
    modifier: Modifier = Modifier,
    nbpViewModel: NbpViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    widget: GoldPrice,
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<GoldPriceWrapper>?>(null) }
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(widget) {
        do {

            // Set data to null to display progress during data load
            result = null
            delay(500)

            // `toMutableList` is used only to crate new reference of the list.
            // New reference is needed to invoke recomposition.
            result = nbpViewModel.fetchGoldPriceData(widget)
            delay(settings.refreshInterval)
        } while (isActive)
    }

    DisposableEffect(widget) {
        onDispose {
            isActive = false
        }
    }

    GoldPriceWidgetCard(modifier, result)
}

@Composable
fun CurrencyWidget(
    modifier: Modifier = Modifier,
    nbpViewModel: NbpViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    currency: Currency,
) {
    val settings by remember { mutableStateOf(settingsViewModel.readSettings()) }
    var result by remember { mutableStateOf<Result<CurrencyWrapper>?>(null) }
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(currency) {
        do {

            // Set data to null to display progress during data load
            result = null
            delay(500)

            // `toMutableList` is used only to crate new reference of the list.
            // New reference is needed to invoke recomposition.
            result = nbpViewModel.fetchCurrencyData(currency)
            delay(settings.refreshInterval)
        } while (isActive)
    }

    DisposableEffect(currency) {
        onDispose {
            isActive = false
        }
    }
    
    CurrencyWidgetCard(modifier, result, currency)
}

@Composable
private fun GoldPriceWidgetCard(
    modifier: Modifier = Modifier,
    result: Result<GoldPriceWrapper>?
) {
    Card(
        modifier
            .padding(8.dp),
        elevation = 8.dp,
        border = BorderStroke(1.dp,Color(33, 35, 39)),
        shape = MaterialTheme.shapes.small.copy(all = CornerSize(16.dp)),
        backgroundColor = Color(27, 29, 33)
    ) {
        when {
            result == null -> Progress()
            result.isFailure -> ErrorView(
                Modifier.fillMaxSize(),
                message = result.exceptionOrNull()!!.message
            )
            else -> {
                Column(
                    Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val nonNullResult = result.getOrNull()
                        ?: throw IllegalStateException("Result cannot be null")
                    Chart(
                        Modifier
                            .fillMaxSize()
                            .weight(1f), currencyData = nonNullResult.goldPrices,
                        chartColor = Color(255,215,0)
                    )
                    GoldPriceTitle(Modifier, date = nonNullResult.date)
                }
            }
        }
    }
}

@Composable
private fun CurrencyWidgetCard(
    modifier: Modifier = Modifier,
    result: Result<CurrencyWrapper>?,
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
        when {
            result == null -> Progress()
            result.isFailure -> ErrorView(
                Modifier.fillMaxSize(),
                message = result.exceptionOrNull()!!.message
            )
            else -> {
                Column(
                    Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val nonNullResult = result.getOrNull()
                        ?: throw IllegalStateException("Result cannot be null")
                    Chart(
                        Modifier
                            .fillMaxSize()
                            .weight(1f), currencyData = nonNullResult.rates
                    )
                    CurrencyTitle(Modifier, widget = widget, date = nonNullResult.date)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ErrorView(
    modifier: Modifier = Modifier,
    message: String? = "Unknown error"
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            text = message ?: "Unknown error",
            color = Color(239,  83, 80)
        )
    }
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

@Composable
private fun Chart(
    modifier: Modifier = Modifier,
    currencyData: List<Float>,
    chartColor: Color = Color.White
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
                .padding(start = 21.dp, top = 4.dp, end = 4.dp, bottom = 8.dp),
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
                        chartColor,
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
private fun ChartDescription(data: List<Float>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 8.dp),
        onDraw = {

            // Obtain left offset and others, different for small and big values
            val hasBigValues = data.first() > 100F
            val minValue = data.minOrNull() ?: return@Canvas
            val maxValue = data.maxOrNull() ?: return@Canvas
            val latest = data.last()
            val heightRatio = size.height / (maxValue - minValue)

            val leftPosition = (if (hasBigValues) 18.dp else 16.dp).toPx()
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
                    fun obtainFormat(value: Float) =
                        if (hasBigValues) "%.0f".format(value)
                        else "%.2f".format(maxValue)

                    val maxText = obtainFormat(maxValue)
                    val minText = obtainFormat(minValue)
                    val latestText = obtainFormat(latest)

                    drawText(maxText, 0F, 8F, paint)
                    drawText(minText, 0F, (maxValue - minValue) * heightRatio + 8F, paint)
                    if (showLatest) {
                        drawText(latestText, 0F, (maxValue - latest) * heightRatio + 8F, paint)
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
        Result.success(CurrencyWrapper("2022-09-06", listOf(4.56f, 4.57f, 4.60f, 4.59f, 4.53f, 4.61f, 4.64f, 4.69f, 4.68f, 4.65f, 4.60f, 4.56f))),
        Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.USD)
    )
}

@Preview
@Composable
fun GoldPriceWidgetPreview() {
    GoldPriceWidgetCard(
        Modifier.size(250.dp, 200.dp),
        Result.success(GoldPriceWrapper("2022-09-06", listOf(7372F, 7370F, 7400F, 7380F, 7390F, 7385F, 7395F, 7390F)))
    )
}

@Preview
@Composable
fun CurrencyWidgetErrorPreview() {
    CurrencyWidgetCard(
        Modifier.size(250.dp, 200.dp),
        Result.failure(IllegalStateException("HTTP 500")),
        Currency(Currency.CurrencyType.PLN, Currency.CurrencyType.USD)
    )
}