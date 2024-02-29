package com.raveline.stockmarketapplication.presentation.screen.company_info_screen.components

import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import java.time.LocalDateTime
import kotlin.math.round
import kotlin.math.roundToInt

/**
 * Composable function to display a stock chart.
 *
 * @param modifier The modifier for the composable.
 * @param intraDayList List of intra-day information models representing stock data points.
 * @param graphColor The color of the graph line.
 */
@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    intraDayList: List<IntraDayInfoModel> = emptyList(),
    graphColor: Color = Color.Green
) {
    // Spacing between elements on the chart
    val spacing = 100f

    // Transparent version of the graph color
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    // Calculate the upper and lower value limits for the y-axis
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

    // Local density instance for text sizing
    val density = LocalDensity.current

    // Paint object for drawing text
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // Draw the chart
    Canvas(
        modifier = modifier,
        onDraw = {
            // Calculate space per hour
            val spacePerHour = (size.width - spacing) / intraDayList.size

            // Draw hour labels
            (intraDayList.indices step 2).forEach { index ->
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

            // Calculate price steps
            val priceStep = (upperValue - lowerValue) / 5f

            // Draw price labels
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

            // Draw the graph line
            var lastX = 0f
            val strokePath = Path().apply {
                val height = size.height
                for (i in intraDayList.indices) {
                    val intraInfo = intraDayList[i]
                    val nextIntraInfo = intraDayList.getOrNull(i + 1) ?: intraDayList.last()
                    val leftRatio = (intraInfo.close - lowerValue) / (upperValue - lowerValue)
                    val rightRatio = (nextIntraInfo.close - lowerValue) / (upperValue - lowerValue)
                    val x = spacing + i * spacePerHour
                    val nextX = spacing + (i + 1) * spacePerHour
                    val y = height - spacing - (leftRatio * height).toFloat()
                    val nextY = height - spacing - (rightRatio * height).toFloat()
                    if (i == 0) {
                        moveTo(x, y)
                    }
                    lastX = (x + nextX) / 2f
                    quadraticBezierTo(
                        x,
                        y,
                        lastX,
                        (y + nextY) / 2f
                    )
                }
            }

            // Create a filled path
            val fillPath = android.graphics.Path(strokePath.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(lastX, size.height - spacing)
                    lineTo(spacing, size.height - spacing)
                    close()
                }

            // Draw the filled path with a vertical gradient
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColor,
                        Color.Transparent,
                    ),
                    endY = size.height - spacing
                ),
            )

            // Draw the graph line
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            )
        },
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrevStockChart() {
    StockChart(
        intraDayList = listOf(
            IntraDayInfoModel(
                date = LocalDateTime.now(),
                close = 100.0
            ),
            IntraDayInfoModel(
                date = LocalDateTime.now().plusHours(1),
                close = 110.0
            ),
            IntraDayInfoModel(
                date = LocalDateTime.now().plusHours(2),
                close = 120.0
            ),
            IntraDayInfoModel(
                date = LocalDateTime.now().plusHours(3),
                close = 130.0
            ),
            IntraDayInfoModel(
                date = LocalDateTime.now().plusHours(4),
                close = 140.0
            ),
            IntraDayInfoModel(
                date = LocalDateTime.now().plusHours(5),
                close = 150.0
            ),
        ),
    )
}