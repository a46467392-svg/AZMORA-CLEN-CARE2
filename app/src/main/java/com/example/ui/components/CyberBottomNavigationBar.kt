package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
)

@Composable
fun CyberBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    // Exactly 4 primary items: Home/Clean, RAM, Battery, Storage
    val items = listOf(
        NavItem(Screen.HOME, "Home", Icons.Default.Dashboard),
        NavItem(Screen.CLEANER, "Clean", Icons.Default.CleaningServices),
        NavItem(Screen.RAM_OPTIMIZER, "RAM", Icons.Default.Speed),
        NavItem(Screen.BATTERY_MONITOR, "Battery", Icons.Default.BatteryChargingFull),
        NavItem(Screen.STORAGE_CLEANER, "Storage", Icons.Default.PieChart)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .shadow(18.dp, shape = RoundedCornerShape(26.dp), ambientColor = NeonCyan.copy(alpha = 0.25f))
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF090E1B).copy(alpha = 0.96f))
            .border(
                1.2.dp,
                Brush.horizontalGradient(listOf(CyberCardBorder, NeonCyan.copy(alpha = 0.5f), CyberCardBorder)),
                RoundedCornerShape(26.dp)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(64.dp)
        ) {
            items.forEach { item ->
                val selected = currentScreen == item.screen
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.screen) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selected) CyberBlack else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = if (selected) NeonCyan else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = NeonCyan,
                        selectedIconColor = CyberBlack,
                        unselectedIconColor = TextSecondary,
                        selectedTextColor = NeonCyan,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    }
}
