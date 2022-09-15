package com.darekbx.dashboard.ui.common

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.ui.widgets.WidgetCard
import kotlinx.coroutines.delay
import java.lang.IllegalStateException
import kotlin.math.abs

enum class RefreshState {
    CLEAR,
    LOAD
}

@Preview
@Composable
fun Progress() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}

@Preview
@Composable
fun ErrorView(
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
fun RefreshableContent(
    refreshInterval: Long,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    invokeRefresh: suspend (RefreshState) -> Unit
) {
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        do {

            // Set data to null to display progress during data load
            invokeRefresh(RefreshState.CLEAR)
            delay(500)

            // `toMutableList` is used only to crate new reference of the list.
            // New reference is needed to invoke recomposition.
            invokeRefresh(RefreshState.LOAD)
            delay(refreshInterval)
        } while (isActive)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event){
                Lifecycle.Event.ON_STOP  -> {
                    isActive = false
                }
                Lifecycle.Event.ON_START  -> {
                    isActive = true
                }
                else -> { }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun ChartTitle(
    modifier: Modifier = Modifier,
    name: String,
    date: String
) {
    val text = buildAnnotatedString {
        append(name)
        withStyle(
            SpanStyle(
                fontSize = 8.sp,
                color = Color.LightGray,
                // Used to center date info
                baselineShift = BaselineShift(0.11F)
            )
        ) {
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
fun Chart(
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
                .padding(start = 34.dp, top = 4.dp, end = 4.dp, bottom = 8.dp),
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
                        strokeWidth = 2F,
                        cap = StrokeCap.Round,
                        start = Offset(firstPoint.x, firstPoint.y),
                        end = Offset(x, y)
                    )

                    firstPoint.x = x
                    firstPoint.y = y
                }
            }
        )
    }
}

@Composable
fun CommonWidgetCard(
    modifier: Modifier = Modifier,
    result: Result<CommonWrapper>?,
    chartColor: Color,
    title: @Composable () -> Unit,
) {
    WidgetCard(modifier.padding(8.dp)) {
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
                            .weight(1f), currencyData = nonNullResult.rates,
                        chartColor = chartColor
                    )
                    title()
                }
            }
        }
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
                        else "%.2f".format(value)

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