package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StorageCategoryInfo
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberCircularMeter
import com.example.ui.theme.*
import com.example.util.FormatUtils
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

@Composable
fun StorageCleanerScreen(
    viewModel: CleanerViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

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
                    text = "Storage Cleaner",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Disk Analyzer & Category Breakdown",
                    color = AccentPurple,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Circular Storage Usage Chart
        CyberCircularMeter(
            percentage = state.storageInfo.usagePercentage,
            size = 180.dp,
            strokeWidth = 13.dp,
            primaryColor = AccentPurple,
            secondaryColor = ElectricBlue,
            label = "STORAGE USED",
            subLabel = "${FormatUtils.formatFileSize(state.storageInfo.usedStorageBytes)} / ${FormatUtils.formatFileSize(state.storageInfo.totalStorageBytes)}"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Storage Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CyberCard(
                modifier = Modifier.weight(1f),
                borderColor = AccentPurple.copy(alpha = 0.3f)
            ) {
                Text("Total Space", color = TextSecondary, fontSize = 11.sp)
                Text(
                    FormatUtils.formatFileSize(state.storageInfo.totalStorageBytes),
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            CyberCard(
                modifier = Modifier.weight(1f),
                borderColor = NeonGreen.copy(alpha = 0.3f)
            ) {
                Text("Free Space", color = TextSecondary, fontSize = 11.sp)
                Text(
                    FormatUtils.formatFileSize(state.storageInfo.freeStorageBytes),
                    color = NeonGreen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Clean Storage Action
        CyberButton(
            text = "ANALYZE & CLEAN STORAGE",
            onClick = { viewModel.navigateTo(Screen.CLEANER) },
            icon = Icons.Default.FolderDelete,
            modifier = Modifier.testTag("clean_storage_button")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Breakdown List
        Text(
            text = "STORAGE BY CATEGORY",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 4.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.storageInfo.categories) { category ->
                StorageCategoryCard(
                    category = category,
                    onClick = {
                        if (category.id == "apps") {
                            viewModel.openAppStorage(viewModel.getApplication<android.app.Application>().packageName)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun StorageCategoryCard(
    category: StorageCategoryInfo,
    onClick: () -> Unit
) {
    val categoryIcon = when (category.iconName) {
        "Apps" -> Icons.Default.Apps
        "Image" -> Icons.Default.Image
        "VideoLibrary" -> Icons.Default.VideoLibrary
        "AudioFile" -> Icons.Default.AudioFile
        else -> Icons.Default.Folder
    }

    val color = Color(category.colorHex)

    CyberCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = color.copy(alpha = 0.3f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f))
                    .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = category.name,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = category.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = FormatUtils.formatFileSize(category.sizeBytes),
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { category.percentage.coerceIn(0.01f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = color,
                    trackColor = CyberCardBg
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${category.itemCount} files indexed • ${(category.percentage * 100).toInt()}% of storage",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
