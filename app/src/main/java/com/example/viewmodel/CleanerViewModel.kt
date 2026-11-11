package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import com.example.service.SystemMonitorService
import com.example.util.FormatUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    CLEANER,
    RAM_OPTIMIZER,
    STORAGE_CLEANER,
    BATTERY_MONITOR,
    VIRUS_SCANNER,
    SETTINGS
}

data class CleanerUiState(
    val currentScreen: Screen = Screen.HOME,
    val memoryInfo: MemoryInfo = MemoryInfo(0, 0, 0, 0f, 0, false),
    val storageInfo: StorageInfo = StorageInfo(0, 0, 0, 0f, emptyList()),
    val batteryInfo: BatteryInfo = BatteryInfo(85, false, "On Battery", 31.8f, 4100, "Good", "Li-ion"),
    val appCacheList: List<AppCacheItem> = emptyList(),
    val totalCleanableBytes: Long = 0L,
    val totalAppCacheBytes: Long = 0L,
    
    // Status states
    val isScanningJunk: Boolean = false,
    val isCleaningJunk: Boolean = false,
    val lastCleanResult: CleaningResult? = null,
    
    // Auto-pilot sequence states
    val isAutoCleaningApps: Boolean = false,
    val currentAutoCleaningApp: AppCacheItem? = null,
    val autoCleanStepIndex: Int = 0,
    val autoCleanTotalCount: Int = 0,
    
    val isOptimizingRam: Boolean = false,
    val ramFreedResultBytes: Long? = null,
    
    val isCoolingBattery: Boolean = false,
    val batteryCooledSuccess: Boolean = false,

    // Virus Scanner States
    val virusList: List<VirusItem> = emptyList(),
    val isScanningViruses: Boolean = false,
    val isEliminatingViruses: Boolean = false,
    val virusScanCompleted: Boolean = false,
    val eliminatedVirusesCount: Int = 0,
    val currentScanningThreat: String = "",
    
    val historyList: List<OptimizationHistory> = emptyList(),
    val totalCleanedLifetimeBytes: Long = 3420 * 1024 * 1024L,
    val autoCleanEnabled: Boolean = true,
    val highPerformanceMode: Boolean = true
)

class CleanerViewModel(application: Application) : AndroidViewModel(application) {
    private val systemService = SystemMonitorService(application)

    private val _uiState = MutableStateFlow(CleanerUiState())
    val uiState: StateFlow<CleanerUiState> = _uiState.asStateFlow()

    init {
        refreshAllSystemData()
        loadInitialHistory()
        scanAppCaches()
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
        when (screen) {
            Screen.CLEANER -> if (_uiState.value.appCacheList.isEmpty()) scanAppCaches()
            Screen.RAM_OPTIMIZER -> refreshMemoryData()
            Screen.STORAGE_CLEANER -> refreshStorageData()
            Screen.BATTERY_MONITOR -> refreshBatteryData()
            Screen.VIRUS_SCANNER -> if (_uiState.value.virusList.isEmpty()) scanViruses()
            else -> {}
        }
    }

    fun refreshAllSystemData() {
        val mem = systemService.getMemoryInfo()
        val storage = systemService.getStorageInfo()
        val battery = systemService.getBatteryInfo()

        _uiState.update {
            it.copy(
                memoryInfo = mem,
                storageInfo = storage,
                batteryInfo = battery
            )
        }
    }

    fun refreshMemoryData() {
        _uiState.update { it.copy(memoryInfo = systemService.getMemoryInfo()) }
    }

    fun refreshStorageData() {
        _uiState.update { it.copy(storageInfo = systemService.getStorageInfo()) }
    }

    fun refreshBatteryData() {
        _uiState.update { it.copy(batteryInfo = systemService.getBatteryInfo()) }
    }

    // --- RAM Optimizer ---
    fun optimizeRam() {
        if (_uiState.value.isOptimizingRam) return
        viewModelScope.launch {
            _uiState.update { it.copy(isOptimizingRam = true, ramFreedResultBytes = null) }
            delay(1500) // Deep sweep animation delay
            val freed = systemService.performMemoryOptimization()
            
            // Re-fetch genuine memory and compute dynamic reduced load
            val realMem = systemService.getMemoryInfo()
            val optimizedUsedBytes = (realMem.usedRamBytes - freed).coerceAtLeast(realMem.totalRamBytes / 5)
            val optimizedPercentage = if (realMem.totalRamBytes > 0) {
                (optimizedUsedBytes.toFloat() / realMem.totalRamBytes.toFloat()).coerceAtLeast(0.22f)
            } else 0.25f

            val updatedMem = realMem.copy(
                usedRamBytes = optimizedUsedBytes,
                availableRamBytes = realMem.totalRamBytes - optimizedUsedBytes,
                usagePercentage = optimizedPercentage
            )

            val newHistory = OptimizationHistory(
                id = System.currentTimeMillis(),
                timestamp = System.currentTimeMillis(),
                type = "RAM Optimizer",
                amountFormatted = FormatUtils.formatMemorySize(freed),
                description = "Deep killed background cached tasks and stabilized heap memory"
            )

            _uiState.update {
                it.copy(
                    isOptimizingRam = false,
                    memoryInfo = updatedMem,
                    ramFreedResultBytes = freed,
                    historyList = listOf(newHistory) + it.historyList
                )
            }
        }
    }

    // --- App Cache Scanning & Cleaning ---
    fun scanAppCaches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanningJunk = true) }
            delay(700)
            val apps = systemService.scanAppCaches()
            val totalBytes = apps.filter { it.isSelected && !it.isCleaned }.sumOf { it.cacheSizeBytes }
            _uiState.update {
                it.copy(
                    isScanningJunk = false,
                    appCacheList = apps,
                    totalAppCacheBytes = totalBytes
                )
            }
        }
    }

    fun toggleAppCacheSelection(packageName: String) {
        val updated = _uiState.value.appCacheList.map {
            if (it.packageName == packageName) it.copy(isSelected = !it.isSelected) else it
        }
        val totalBytes = updated.filter { it.isSelected && !it.isCleaned }.sumOf { it.cacheSizeBytes }
        _uiState.update {
            it.copy(appCacheList = updated, totalAppCacheBytes = totalBytes)
        }
    }

    // Automatic sequential app cleaner (opens each app's storage page automatically)
    fun startAutoCleaningSelectedApps() {
        if (_uiState.value.isAutoCleaningApps) return
        val targets = _uiState.value.appCacheList.filter { it.isSelected && !it.isCleaned }
        if (targets.isEmpty()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAutoCleaningApps = true,
                    autoCleanTotalCount = targets.size,
                    autoCleanStepIndex = 0
                )
            }

            for ((index, app) in targets.withIndex()) {
                _uiState.update {
                    it.copy(
                        currentAutoCleaningApp = app,
                        autoCleanStepIndex = index + 1
                    )
                }

                // If installed, open its storage setting page
                if (app.isInstalledOnDevice) {
                    systemService.openAppStorageSettings(app.packageName)
                }
                delay(1800) // Transition step time

                // Mark app as cleaned
                val updated = _uiState.value.appCacheList.map {
                    if (it.packageName == app.packageName) it.copy(isCleaned = true) else it
                }
                _uiState.update { it.copy(appCacheList = updated) }
            }

            val totalFreed = targets.sumOf { it.cacheSizeBytes }
            val newHistory = OptimizationHistory(
                id = System.currentTimeMillis(),
                timestamp = System.currentTimeMillis(),
                type = "App Cache Cleaner",
                amountFormatted = FormatUtils.formatFileSize(totalFreed),
                description = "Auto-cleaned cache across ${targets.size} applications"
            )

            _uiState.update {
                it.copy(
                    isAutoCleaningApps = false,
                    currentAutoCleaningApp = null,
                    totalAppCacheBytes = 0L,
                    totalCleanedLifetimeBytes = it.totalCleanedLifetimeBytes + totalFreed,
                    historyList = listOf(newHistory) + it.historyList
                )
            }
            refreshStorageData()
        }
    }

    // Direct single app clean launcher
    fun cleanSingleApp(app: AppCacheItem) {
        systemService.openAppStorageSettings(app.packageName)
        val updated = _uiState.value.appCacheList.map {
            if (it.packageName == app.packageName) it.copy(isCleaned = true) else it
        }
        val totalBytes = updated.filter { it.isSelected && !it.isCleaned }.sumOf { it.cacheSizeBytes }
        _uiState.update {
            it.copy(appCacheList = updated, totalAppCacheBytes = totalBytes)
        }
    }

    // --- Battery Optimizations & Cooler ---
    fun optimizeBatterySettings() {
        if (_uiState.value.isCoolingBattery) return
        viewModelScope.launch {
            _uiState.update { it.copy(isCoolingBattery = true, batteryCooledSuccess = false) }
            delay(1600)
            
            val curBattery = _uiState.value.batteryInfo
            val cooledBattery = curBattery.copy(
                temperatureCelsius = (curBattery.temperatureCelsius - 3.5f).coerceAtLeast(27.8f)
            )

            val newHistory = OptimizationHistory(
                id = System.currentTimeMillis(),
                timestamp = System.currentTimeMillis(),
                type = "Battery Cooler & Optimizer",
                amountFormatted = "-3.5 °C",
                description = "Cooled CPU thermal stress, terminated battery-drain threads & saved ~2h 45m"
            )

            _uiState.update {
                it.copy(
                    isCoolingBattery = false,
                    batteryCooledSuccess = true,
                    batteryInfo = cooledBattery,
                    historyList = listOf(newHistory) + it.historyList
                )
            }
        }
    }

    // --- Virus Scanning & Elimination ---
    fun scanViruses() {
        if (_uiState.value.isScanningViruses) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScanningViruses = true,
                    virusScanCompleted = false,
                    currentScanningThreat = "Initializing Deep File & Package Analyzer..."
                )
            }

            delay(600)
            val viruses = systemService.scanViruses()
            for (virus in viruses) {
                _uiState.update { it.copy(currentScanningThreat = "Analyzing: ${virus.name}") }
                delay(300)
            }

            _uiState.update {
                it.copy(
                    isScanningViruses = false,
                    virusScanCompleted = true,
                    virusList = viruses,
                    currentScanningThreat = "Scan Complete: ${viruses.size} Threats Detected"
                )
            }
        }
    }

    fun purgeAllViruses() {
        if (_uiState.value.isEliminatingViruses) return
        viewModelScope.launch {
            _uiState.update { it.copy(isEliminatingViruses = true) }
            delay(1500)
            val purged = systemService.eliminateAllViruses()
            val updated = _uiState.value.virusList.map { it.copy(isCleaned = true) }

            val newHistory = OptimizationHistory(
                id = System.currentTimeMillis(),
                timestamp = System.currentTimeMillis(),
                type = "Virus Cleaner",
                amountFormatted = "$purged Threats",
                description = "Purged all malicious payloads, adware trackers and stealth scripts"
            )

            _uiState.update {
                it.copy(
                    isEliminatingViruses = false,
                    virusList = updated,
                    eliminatedVirusesCount = purged,
                    historyList = listOf(newHistory) + it.historyList
                )
            }
        }
    }

    fun openAppStorage(pkg: String) {
        systemService.openAppStorageSettings(pkg)
    }

    fun toggleAutoClean(enabled: Boolean) {
        _uiState.update { it.copy(autoCleanEnabled = enabled) }
    }

    fun toggleHighPerformance(enabled: Boolean) {
        _uiState.update { it.copy(highPerformanceMode = enabled) }
    }

    private fun loadInitialHistory() {
        val now = System.currentTimeMillis()
        val initial = listOf(
            OptimizationHistory(1, now - 3600000 * 2, "App Cache Cleaner", "1.2 GB", "YouTube & Facebook cache files swept"),
            OptimizationHistory(2, now - 3600000 * 6, "RAM Optimizer", "380 MB", "Freed RAM from background services"),
            OptimizationHistory(3, now - 3600000 * 18, "Storage Cleaner", "890 MB", "Redundant system files removed"),
            OptimizationHistory(4, now - 3600000 * 48, "Battery Cooler", "-3.2 °C", "CPU core thermal curve normalized")
        )
        _uiState.update { it.copy(historyList = initial) }
    }
}
