package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppCacheItem
import com.example.ui.components.BrandAppIconBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberCircularMeter
import com.example.ui.theme.*
import com.example.util.FormatUtils
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun CleanerScreen(
    viewModel: CleanerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val uncleanedApps = state.appCacheList.filter { !it.isCleaned }
    val totalUncleanedBytes = uncleanedApps.filter { it.isSelected }.sumOf { it.cacheSizeBytes }

    Column(
        modifier = modifier
            .fillMaxSize()
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
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberCardBg)
                    .border(1.2.dp, CyberCardBorder, RoundedCornerShape(12.dp))
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
                    text = "App Cache Cleaner",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Individual App Logos • Auto-Pilot Cleaning",
                    color = NeonCyan,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Circular Scanning Meter
        val junkPct = if (totalUncleanedBytes > 0) {
            (totalUncleanedBytes.toFloat() / (4000f * 1024f * 1024f)).coerceIn(0.2f, 0.95f)
        } else {
            0.05f
        }

        CyberCircularMeter(
            percentage = if (state.isAutoCleaningApps) 0.9f else junkPct,
            size = 170.dp,
            strokeWidth = 12.dp,
            primaryColor = NeonCyan,
            secondaryColor = ElectricBlue,
            label = if (state.isAutoCleaningApps) "AUTO-CLEANING" else "TOTAL CACHE",
            subLabel = FormatUtils.formatFileSize(totalUncleanedBytes)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Auto-clean in progress active HUD banner
        AnimatedVisibility(visible = state.isAutoCleaningApps) {
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                borderColor = NeonCyan,
                glowAlpha = 0.35f
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = NeonCyan,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-Opening App Storage (${state.autoCleanStepIndex}/${state.autoCleanTotalCount})",
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Navigating to ${state.currentAutoCleaningApp?.appName ?: "app"}...",
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Action Buttons Row: Auto-Clean Sequence + Re-scan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberButton(
                text = if (state.isAutoCleaningApps) "CLEANING APPS..." else "AUTO-CLEAN APPS",
                onClick = { viewModel.startAutoCleaningSelectedApps() },
                icon = Icons.Default.AutoMode,
                enabled = !state.isAutoCleaningApps && uncleanedApps.isNotEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .testTag("auto_clean_apps_button")
            )

            Button(
                onClick = { viewModel.scanAppCaches() },
                modifier = Modifier
                    .height(58.dp)
                    .clip(RoundedCornerShape(29.dp))
                    .background(CyberCardBg)
                    .border(1.2.dp, CyberCardBorder, RoundedCornerShape(29.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Scan",
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Apps List Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "APPLICATIONS & CACHE ACCUMULATION",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "${uncleanedApps.size} apps detected",
                color = NeonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // App-by-App list featuring real logos for YouTube, Facebook, Chrome, TikTok, etc.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.appCacheList) { app ->
                AppCacheItemRow(
                    app = app,
                    onToggleSelect = { viewModel.toggleAppCacheSelection(app.packageName) },
                    onCleanClick = { viewModel.cleanSingleApp(app) }
                )
            }
        }
    }
}

@Composable
private fun AppCacheItemRow(
    app: AppCacheItem,
    onToggleSelect: () -> Unit,
    onCleanClick: () -> Unit
) {
    val isCleaned = app.isCleaned
    val borderColor = when {
        isCleaned -> NeonGreen.copy(alpha = 0.35f)
        app.cacheSizeBytes > 900 * 1024 * 1024L -> NeonRed.copy(alpha = 0.45f)
        else -> CyberCardBorder
    }

    CyberCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = borderColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox on the left
            Checkbox(
                checked = app.isSelected && !isCleaned,
                onCheckedChange = { if (!isCleaned) onToggleSelect() },
                enabled = !isCleaned,
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonCyan,
                    checkmarkColor = CyberBlack,
                    uncheckedColor = TextMuted
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Real Brand Logo (YouTube Red, Facebook Blue, TikTok Dark, Chrome Blue, etc.)
            BrandAppIconBadge(packageName = app.packageName, size = 42.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = app.appName,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = if (isCleaned) "0 B" else FormatUtils.formatFileSize(app.cacheSizeBytes),
                        color = if (isCleaned) NeonGreen else if (app.cacheSizeBytes > 900 * 1024 * 1024L) NeonRed else BrightCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCleaned) "Cache Cleared" else "Accumulated cache • ${app.categoryLabel}",
                        color = if (isCleaned) NeonGreen else TextSecondary,
                        fontSize = 11.sp
                    )

                    if (!isCleaned) {
                        // Clean Button on each app
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricBlue.copy(alpha = 0.25f))
                                .border(1.2.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .clickable { onCleanClick() }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Clean",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Cleaned",
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
