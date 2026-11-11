package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = CyberBlack,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = Color.White,
    secondary = BrightCyan,
    onSecondary = CyberBlack,
    secondaryContainer = CyberCardBg,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentPurple,
    background = CyberBlack,
    onBackground = TextPrimary,
    surface = CyberDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberCardBg,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = BrightCyan,
    onSecondary = Color.White,
    background = Color(0xFF0D1527),
    onBackground = TextPrimary,
    surface = Color(0xFF152238),
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF1E2E4A),
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF2563EB)
)

@Composable
fun SmartCleanerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
