package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.util.FormatUtils
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun RamOptimizerScreen(
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
                    text = "RAM Optimizer",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hardware Memory Governor",
                    color = BrightCyan,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Circular Animated RAM Meter
        CyberCircularMeter(
            percentage = state.memoryInfo.usagePercentage,
            size = 200.dp,
            strokeWidth = 14.dp,
            primaryColor = BrightCyan,
            secondaryColor = ElectricBlue,
            label = "RAM LOAD",
            subLabel = "${FormatUtils.formatMemorySize(state.memoryInfo.usedRamBytes)} / ${FormatUtils.formatMemorySize(state.memoryInfo.totalRamBytes)}"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // RAM Stats Card
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = BrightCyan.copy(alpha = 0.35f)
        ) {
            Text(
                text = "REAL-TIME RAM METRICS",
                color = BrightCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            RamMetricRow(
                title = "Total Physical RAM",
                value = FormatUtils.formatMemorySize(state.memoryInfo.totalRamBytes),
                indicatorColor = TextSecondary
            )
            HorizontalDivider(
                color = CyberCardBorder.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            RamMetricRow(
                title = "Active Memory (In Use)",
                value = FormatUtils.formatMemorySize(state.memoryInfo.usedRamBytes),
                indicatorColor = NeonAmber
            )
            HorizontalDivider(
                color = CyberCardBorder.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            RamMetricRow(
                title = "Available / Free Memory",
                value = FormatUtils.formatMemorySize(state.memoryInfo.availableRamBytes),
                indicatorColor = NeonGreen
            )
            HorizontalDivider(
                color = CyberCardBorder.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            RamMetricRow(
                title = "Low Memory Threshold",
                value = FormatUtils.formatMemorySize(state.memoryInfo.lowMemoryThreshold),
                indicatorColor = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Ram Optimization Result Banner
        if (state.ramFreedResultBytes != null) {
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
                            text = "Memory Optimization Complete",
                            color = NeonGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Freed ~${FormatUtils.formatMemorySize(state.ramFreedResultBytes!!)} and stabilized system trim",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Action Button
        CyberButton(
            text = if (state.isOptimizingRam) "OPTIMIZING RAM..." else "OPTIMIZE RAM NOW",
            onClick = { viewModel.optimizeRam() },
            icon = Icons.Default.Speed,
            enabled = !state.isOptimizingRam,
            modifier = Modifier.testTag("optimize_ram_button")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Safe Optimization Recommendations
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = CyberCardBorder
        ) {
            Text(
                text = "MEMORY EFFICIENCY TIPS",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            MemoryTipItem(
                title = "Background Process Trimming",
                description = "Smart Cleaner cleans cached activity allocations without terminating persistent user alarms or notifications."
            )
            Spacer(modifier = Modifier.height(8.dp))
            MemoryTipItem(
                title = "Android LMK (Low Memory Killer)",
                description = "Modern Android automatically prioritizes foreground tasks. Optimization assists by pre-releasing stale buffers."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RamMetricRow(
    title: String,
    value: String,
    indicatorColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp
            )
        }
        Text(
            text = value,
            color = indicatorColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MemoryTipItem(
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = BrightCyan,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
