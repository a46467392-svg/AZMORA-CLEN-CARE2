package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.RiskLevel
import com.example.model.VirusItem
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberCircularMeter
import com.example.ui.theme.*
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun VirusScannerScreen(
    viewModel: CleanerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val activeThreats = state.virusList.filter { !it.isCleaned }

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
                    text = "Virus & Threat Cleaner",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "One-Tap Full Device Disinfection",
                    color = NeonRed,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Circular Scanning Meter / Threat Count
        CyberCircularMeter(
            percentage = if (activeThreats.isNotEmpty()) 0.85f else 0.0f,
            size = 170.dp,
            strokeWidth = 12.dp,
            primaryColor = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
            secondaryColor = ElectricBlue,
            label = if (state.isScanningViruses) "SCANNING" else if (activeThreats.isNotEmpty()) "THREATS FOUND" else "100% CLEAN",
            subLabel = "${activeThreats.size} Viruses Detected"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // High Quality Futuristic ZERO VIRUS Badge
        com.example.ui.components.ZeroVirusBadge(
            modifier = Modifier.fillMaxWidth(),
            isClean = activeThreats.isEmpty(),
            threatCount = activeThreats.size
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Scanning Status HUD
        if (state.isScanningViruses || state.isEliminatingViruses) {
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                borderColor = NeonRed
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = NeonRed,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (state.isEliminatingViruses) "Purging all viruses from device storage..." else state.currentScanningThreat,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Action Button: One-Tap Purge All Viruses
        CyberButton(
            text = when {
                state.isEliminatingViruses -> "ELIMINATING VIRUSES..."
                activeThreats.isNotEmpty() -> "DELETE ALL VIRUSES NOW (${activeThreats.size})"
                else -> "SCAN FOR VIRUSES"
            },
            onClick = {
                if (activeThreats.isNotEmpty()) {
                    viewModel.purgeAllViruses()
                } else {
                    viewModel.scanViruses()
                }
            },
            icon = Icons.Default.Shield,
            brush = if (activeThreats.isNotEmpty()) DangerButtonBrush else SuccessButtonBrush,
            enabled = !state.isScanningViruses && !state.isEliminatingViruses,
            modifier = Modifier.testTag("delete_viruses_button")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Viruses List Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "THREATS IN STORAGE & FILES",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = if (activeThreats.isNotEmpty()) "${activeThreats.size} Active" else "Protected",
                color = if (activeThreats.isNotEmpty()) NeonRed else NeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Threat List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.virusList) { virus ->
                VirusThreatCard(virus)
            }

            if (state.virusList.isEmpty() && !state.isScanningViruses) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tap 'Scan For Viruses' to verify your device integrity.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VirusThreatCard(virus: VirusItem) {
    val isCleaned = virus.isCleaned
    val borderColor = if (isCleaned) NeonGreen.copy(alpha = 0.35f) else NeonRed.copy(alpha = 0.45f)

    CyberCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = borderColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCleaned) NeonGreen.copy(alpha = 0.15f) else NeonRed.copy(alpha = 0.15f))
                    .border(1.2.dp, borderColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCleaned) Icons.Default.CheckCircle else Icons.Default.BugReport,
                    contentDescription = virus.name,
                    tint = if (isCleaned) NeonGreen else NeonRed,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = virus.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CyberBadge(
                        text = if (isCleaned) "Purged" else virus.threatType,
                        color = if (isCleaned) NeonGreen else NeonRed
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = virus.threatDescription,
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Path: ${virus.locationPath}",
                    color = TextMuted,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }
    }
}
