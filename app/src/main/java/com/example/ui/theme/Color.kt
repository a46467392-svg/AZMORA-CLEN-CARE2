package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Futuristic Deep Midnight Black & Neon Cyan/Electric Blue Color Palette
val CyberBlack = Color(0xFF070B14)
val CyberDarkSurface = Color(0xFF0F172A)
val CyberCardBg = Color(0xFF131D36)
val CyberCardBorder = Color(0xFF1E3A8A)

// Neon Accents
val ElectricBlue = Color(0xFF2563EB)
val NeonCyan = Color(0xFF00F0FF)
val BrightCyan = Color(0xFF38BDF8)
val DeepNavy = Color(0xFF0A0F1D)
val AccentPurple = Color(0xFF8B5CF6)
val NeonGreen = Color(0xFF10B981)
val NeonAmber = Color(0xFFF59E0B)
val NeonRed = Color(0xFFEF4444)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients
val CyberGradient = Brush.linearGradient(
    colors = listOf(ElectricBlue, NeonCyan)
)

val GlowCardBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF1E293B).copy(alpha = 0.85f), Color(0xFF0F172A).copy(alpha = 0.95f))
)

val PrimaryButtonBrush = Brush.horizontalGradient(
    colors = listOf(Color(0xFF1D4ED8), Color(0xFF0284C7), Color(0xFF00F0FF))
)

val DangerButtonBrush = Brush.horizontalGradient(
    colors = listOf(Color(0xFFB91C1C), Color(0xFFEF4444))
)

val SuccessButtonBrush = Brush.horizontalGradient(
    colors = listOf(Color(0xFF047857), Color(0xFF10B981))
)
