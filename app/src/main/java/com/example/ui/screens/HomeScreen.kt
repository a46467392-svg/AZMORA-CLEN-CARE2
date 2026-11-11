package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BrandAppIconBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.theme.*
import com.example.util.FormatUtils
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: CleanerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header: AZMORA Clean Care with Ultra High-Quality AZ Monogram Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ultra High-Quality AZ Monogram Core Shield
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .shadow(18.dp, RoundedCornerShape(16.dp), ambientColor = NeonCyan.copy(alpha = 0.55f))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF070B14))
                        .border(1.8.dp, NeonCyan, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_az_cyber_core_1794395345660),
                        contentDescription = "AZMORA AZ Monogram",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AZMORA",
                            color = NeonCyan,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricBlue.copy(alpha = 0.35f))
                                .border(1.dp, NeonCyan.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PRO FREE",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Text(
                        text = "Clean Care • Performance Core",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = { viewModel.navigateTo(Screen.SETTINGS) },
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.2.dp, CyberCardBorder, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section Title: 5 Square Systems (ক্লিন/ফাইল, র্যাম, ব্যাটারি, স্টোরেজ, ভাইরাস)
        Text(
            text = "5 CORE OPTIMIZATION MODULES",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        val uncleanedBytes = state.appCacheList.filter { !it.isCleaned }.sumOf { it.cacheSizeBytes }
        val activeThreats = state.virusList.filter { !it.isCleaned }

        // The 5 Square Modules Grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: 1. Clean & Files (App & File Cache) + 2. RAM Optimizer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SquareTelemetryBox(
                    moduleNumber = "01",
                    title = "App & File Cache",
                    status = if (uncleanedBytes > 0) "Needs Cleaning" else "Spotless Clean",
                    mainMetric = FormatUtils.formatFileSize(uncleanedBytes.takeIf { it > 0 } ?: 0L),
                    percentage = if (uncleanedBytes > 0) 0.8f else 0.05f,
                    accentColor = NeonCyan,
                    icon = Icons.Default.FolderSpecial,
                    actionLabel = "Clean Apps",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(Screen.CLEANER) }
                )

                SquareTelemetryBox(
                    moduleNumber = "02",
                    title = "RAM Optimizer",
                    status = "${FormatUtils.formatMemorySize(state.memoryInfo.availableRamBytes)} Free",
                    mainMetric = "${(state.memoryInfo.usagePercentage * 100).toInt()}% RAM Used",
                    subMetric = "Total: ${FormatUtils.formatMemorySize(state.memoryInfo.totalRamBytes)}",
                    percentage = state.memoryInfo.usagePercentage,
                    accentColor = BrightCyan,
                    icon = Icons.Default.Speed,
                    actionLabel = "Fresh RAM",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(Screen.RAM_OPTIMIZER) }
                )
            }

            // Row 2: 3. Battery & Cooler + 4. Storage Cleaner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SquareTelemetryBox(
                    moduleNumber = "03",
                    title = "Battery & Cooler",
                    status = if (state.batteryInfo.isCharging) "Fast Charging" else "Optimized Battery",
                    mainMetric = "${state.batteryInfo.percentage}% Charge",
                    subMetric = "Temp: ${state.batteryInfo.temperatureCelsius} °C",
                    percentage = state.batteryInfo.percentage / 100f,
                    accentColor = NeonGreen,
                    icon = Icons.Default.BatteryChargingFull,
                    actionLabel = "Cool Battery",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(Screen.BATTERY_MONITOR) }
                )

                SquareTelemetryBox(
                    moduleNumber = "04",
                    title = "Storage Cleaner",
                    status = "${FormatUtils.formatFileSize(state.storageInfo.freeStorageBytes)} Free",
                    mainMetric = "${(state.storageInfo.usagePercentage * 100).toInt()}% Storage",
                    subMetric = "Total: ${FormatUtils.formatFileSize(state.storageInfo.totalStorageBytes)}",
                    percentage = state.storageInfo.usagePercentage,
                    accentColor = AccentPurple,
                    icon = Icons.Default.PieChart,
                    actionLabel = "Free Disk",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(Screen.STORAGE_CLEANER) }
                )
            }

            // Row 3: 5. Virus Cleaner (৫ নাম্বারে চতুর্ভুজের মতো ভাইরাসের ফোল্ডার / ZERO VIRUS)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF131D36).copy(alpha = 0.95f),
                                Color(0xFF070B14).copy(alpha = 0.98f)
                            )
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(
                                (if (activeThreats.isNotEmpty()) NeonRed else NeonGreen).copy(alpha = 0.7f),
                                CyberCardBorder,
                                (if (activeThreats.isNotEmpty()) NeonRed else NeonCyan).copy(alpha = 0.3f)
                            )
                        ),
                        RoundedCornerShape(22.dp)
                    )
                    .clickable { viewModel.navigateTo(Screen.VIRUS_SCANNER) }
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background((if (activeThreats.isNotEmpty()) NeonRed else NeonGreen).copy(alpha = 0.15f))
                                    .border(1.2.dp, (if (activeThreats.isNotEmpty()) NeonRed else NeonGreen).copy(alpha = 0.45f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (activeThreats.isNotEmpty()) Icons.Default.BugReport else Icons.Default.Shield,
                                    contentDescription = "Virus Cleaner",
                                    tint = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "MODULE 05",
                                color = (if (activeThreats.isNotEmpty()) NeonRed else NeonGreen).copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = if (activeThreats.isNotEmpty()) "PURGE THREATS" else "SYSTEM PROTECTED",
                            color = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Unique Futuristic ZERO VIRUS display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (activeThreats.isNotEmpty()) "${activeThreats.size} THREATS" else "ZERO",
                                    color = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "VIRUS",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                            }
                            Text(
                                text = if (activeThreats.isNotEmpty()) "Detected threats ready for one-tap deletion" else "All storage partitions & file system immunized",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open",
                            tint = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Master One-Tap Optimization Button
        CyberButton(
            text = if (state.isOptimizingRam) "FRESHENING RAM & HARDWARE..." else "EXECUTE PRO CLEAN & BOOST",
            onClick = {
                viewModel.optimizeRam()
                viewModel.optimizeBatterySettings()
            },
            icon = Icons.Default.Bolt,
            modifier = Modifier.testTag("boost_device_button")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Real Apps & Files Cache Quick Preview Section with Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "APP & FILE CACHE PREVIEW",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "View All",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.CLEANER) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Top Apps & Files Cache Preview (Google Files, YouTube, Facebook, TikTok)
        val previewApps = state.appCacheList.take(4)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            previewApps.forEach { app ->
                CyberCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (app.isCleaned) NeonGreen.copy(alpha = 0.3f) else CyberCardBorder,
                    onClick = { viewModel.navigateTo(Screen.CLEANER) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BrandAppIconBadge(packageName = app.packageName, size = 42.dp)

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = app.appName,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (app.isCleaned) "Cache Cleared" else "Accumulated cache • ${app.categoryLabel}",
                                color = if (app.isCleaned) NeonGreen else TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = if (app.isCleaned) "0 B" else FormatUtils.formatFileSize(app.cacheSizeBytes),
                            color = if (app.isCleaned) NeonGreen else BrightCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SquareTelemetryBox(
    moduleNumber: String,
    title: String,
    status: String,
    mainMetric: String,
    percentage: Float,
    accentColor: Color,
    icon: ImageVector,
    actionLabel: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    subMetric: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .shadow(14.dp, shape = RoundedCornerShape(22.dp), ambientColor = accentColor.copy(alpha = 0.25f))
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF131D36).copy(alpha = 0.95f),
                        Color(0xFF070B14).copy(alpha = 0.98f)
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.linearGradient(
                    listOf(
                        accentColor.copy(alpha = 0.6f),
                        CyberCardBorder,
                        accentColor.copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with Module Number & Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.2.dp, accentColor.copy(alpha = 0.45f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = moduleNumber,
                    color = accentColor.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Main Metric Center
            Column {
                Text(
                    text = mainMetric,
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                if (subMetric != null) {
                    Text(
                        text = subMetric,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = status,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            // Progress bar and Action Label
            Column {
                LinearProgressIndicator(
                    progress = { percentage.coerceIn(0.05f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = accentColor,
                    trackColor = CyberCardBg
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = actionLabel,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
