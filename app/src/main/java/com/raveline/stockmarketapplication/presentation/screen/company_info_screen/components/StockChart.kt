package com.raveline.stockmarketapplication.presentation.screen.company_info_screen.components

import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    intraDayList: List<IntraDayInfoModel> = emptyList(),
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(intraDayList) {
        (intraDayList.maxOfOrNull {
            it.close
        }?.plus(1)?.roundToInt()) ?: 0
    }
    val lowerValue = remember(intraDayList) {
        (intraDayList.minOfOrNull {
            it.close
        })?.roundToInt() ?: 0
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Canvas(
        modifier = modifier,
        onDraw = {
            val spacePerHour = (size.width - spacing) / intraDayList.size
            (0 until intraDayList.size - 1 step 2).forEach { index ->
                val intraInfo = intraDayList[index]
                val hour = intraInfo.date.hour
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        hour.toString(),
                        spacing + index * spacePerHour,
                        size.height - 5,
                        textPaint
                    )
                }
            }
            val priceStep = (upperValue - lowerValue) / 5f
            (0 until 5).forEach { index ->
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        round(lowerValue + priceStep * index).toString(),
                        30f,
                        size.height - spacing - index * size.height / 5f,
                        textPaint
                    )
                }
            }
            val strokePath = Path().apply {
                val height = size.height
                for (i in intraDayList.indices) {
                    val intraInfo = intraDayList[i]
                    val nextIntraInfo = intraDayList.getOrNull(i + 1) ?: intraDayList.last()
                    val leftRatio = (intraInfo.close - lowerValue) / (upperValue - lowerValue)
                    val rightRatio = (nextIntraInfo.close - lowerValue) / (upperValue - lowerValue)
                    if (i == 0) {
                        moveTo(
                            spacing + i + spacePerHour,
                            height - spacing - (leftRatio * height).toFloat()
                        )
                    }
                }
            }
        },
    )
}