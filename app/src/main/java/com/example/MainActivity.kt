package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.CyberBottomNavigationBar
import com.example.ui.screens.*
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.SmartCleanerTheme
import com.example.viewmodel.CleanerViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val cleanerViewModel: CleanerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCleanerTheme(darkTheme = true) {
                MainAppContainer(viewModel = cleanerViewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: CleanerViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack),
        containerColor = CyberBlack,
        bottomBar = {
            CyberBottomNavigationBar(
                currentScreen = uiState.currentScreen,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "screenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.HOME -> HomeScreen(viewModel = viewModel)
                    Screen.CLEANER -> CleanerScreen(viewModel = viewModel)
                    Screen.RAM_OPTIMIZER -> RamOptimizerScreen(viewModel = viewModel)
                    Screen.BATTERY_MONITOR -> BatteryMonitorScreen(viewModel = viewModel)
                    Screen.STORAGE_CLEANER -> StorageCleanerScreen(viewModel = viewModel)
                    Screen.VIRUS_SCANNER -> VirusScannerScreen(viewModel = viewModel)
                    Screen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
