package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberCircularMeter
import com.example.ui.theme.*
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun BatteryMonitorScreen(
    viewModel: CleanerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.HOME) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CyberCardBg)
                    .border(1.dp, CyberCardBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Battery Monitor",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Power & Thermal Diagnostics",
                    color = NeonGreen,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Battery Indicator Meter
        val batteryPctFloat = state.batteryInfo.percentage.toFloat() / 100f
        CyberCircularMeter(
            percentage = batteryPctFloat,
            size = 200.dp,
            strokeWidth = 14.dp,
            primaryColor = if (state.batteryInfo.percentage < 20) NeonRed else NeonGreen,
            secondaryColor = ElectricBlue,
            label = if (state.batteryInfo.isCharging) "CHARGING" else "BATTERY LEVEL",
            subLabel = state.batteryInfo.chargingSource
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Battery Telemetry Card
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonGreen.copy(alpha = 0.35f)
        ) {
            Text(
                text = "HARDWARE BATTERY TELEMETRY",
                color = NeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            BatteryMetricRow("Power Source", state.batteryInfo.chargingSource, NeonCyan)
            HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 8.dp))
            BatteryMetricRow("Battery Health", state.batteryInfo.health, NeonGreen)
            HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 8.dp))
            BatteryMetricRow("Thermal Temperature", "${state.batteryInfo.temperatureCelsius} °C", if (state.batteryInfo.temperatureCelsius > 42) NeonRed else TextPrimary)
            HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 8.dp))
            BatteryMetricRow("Voltage", "${state.batteryInfo.voltageMv} mV", TextSecondary)
            HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 8.dp))
            BatteryMetricRow("Chemistry", state.batteryInfo.technology, TextSecondary)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Battery Cooled Banner
        if (state.batteryCooledSuccess) {
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = NeonGreen,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Battery Cooled & Extended!",
                            color = NeonGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Thermal temp decreased to ${state.batteryInfo.temperatureCelsius}°C. Saved ~2h 30m runtime.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Battery Cooler Action
        CyberButton(
            text = if (state.isCoolingBattery) "COOLING BATTERY & CPU..." else "COOL DOWN BATTERY & EXTEND LIFE",
            onClick = { viewModel.optimizeBatterySettings() },
            icon = Icons.Default.AcUnit,
            brush = SuccessButtonBrush,
            enabled = !state.isCoolingBattery,
            modifier = Modifier.testTag("extend_battery_button")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Battery Optimization Recommendations Card
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = CyberCardBorder
        ) {
            Text(
                text = "POWER SAVING RECOMMENDATIONS",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            BatteryTipItem(
                icon = Icons.Default.BrightnessMedium,
                title = "Adaptive Brightness",
                desc = "Screen illumination represents up to 45% of total battery expenditure."
            )
            Spacer(modifier = Modifier.height(8.dp))
            BatteryTipItem(
                icon = Icons.Default.SyncDisabled,
                title = "Restrict Inactive Sync",
                desc = "Turn off background synchronization for dormant accounts and apps."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BatteryMetricRow(
    title: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BatteryTipItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NeonGreen,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(desc, color = TextSecondary, fontSize = 11.sp)
        }
    }
}
