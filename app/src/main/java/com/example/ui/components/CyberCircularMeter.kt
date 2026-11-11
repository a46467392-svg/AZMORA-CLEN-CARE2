package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CyberCircularMeter(
    percentage: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 14.dp,
    primaryColor: Color = NeonCyan,
    secondaryColor: Color = ElectricBlue,
    label: String = "USED",
    subLabel: String = "",
    centerContent: (@Composable () -> Unit)? = null
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "meterPercentage"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val rotateAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotateAngle"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(strokePx / 2, strokePx / 2)

            // Outer decorative ring ticks
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val outerRadius = this.size.width / 2 - 2.dp.toPx()
            val tickCount = 48
            for (i in 0 until tickCount) {
                val angleRad = Math.toRadians((i * (360.0 / tickCount) + rotateAngle)).toFloat()
                val tickLen = if (i % 6 == 0) 8.dp.toPx() else 4.dp.toPx()
                val start = Offset(
                    center.x + (outerRadius - tickLen) * Math.cos(angleRad.toDouble()).toFloat(),
                    center.y + (outerRadius - tickLen) * Math.sin(angleRad.toDouble()).toFloat()
                )
                val end = Offset(
                    center.x + outerRadius * Math.cos(angleRad.toDouble()).toFloat(),
                    center.y + outerRadius * Math.sin(angleRad.toDouble()).toFloat()
                )
                val tickColor = if (i % 6 == 0) primaryColor.copy(alpha = 0.4f) else TextMuted.copy(alpha = 0.15f)
                drawLine(
                    color = tickColor,
                    start = start,
                    end = end,
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            // Background inactive track
            drawArc(
                color = CyberCardBg.copy(alpha = 0.9f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Ambient background glow track
            drawArc(
                color = primaryColor.copy(alpha = 0.15f * pulseAlpha),
                startAngle = 135f,
                sweepAngle = 270f * animatedPercentage,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx + 8.dp.toPx(), cap = StrokeCap.Round)
            )

            // Foreground active neon gradient arc
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to secondaryColor,
                    0.7f to primaryColor,
                    1.0f to BrightCyan,
                    center = center
                ),
                startAngle = 135f,
                sweepAngle = 270f * animatedPercentage,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        if (centerContent != null) {
            centerContent()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${(animatedPercentage * 100).toInt()}%",
                    color = TextPrimary,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = label,
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                if (subLabel.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subLabel,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
