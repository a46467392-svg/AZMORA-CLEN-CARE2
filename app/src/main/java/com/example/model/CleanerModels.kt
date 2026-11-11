package com.example.model

data class MemoryInfo(
    val totalRamBytes: Long,
    val availableRamBytes: Long,
    val usedRamBytes: Long,
    val usagePercentage: Float,
    val lowMemoryThreshold: Long,
    val isLowMemory: Boolean
)

data class StorageCategoryInfo(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val percentage: Float,
    val colorHex: Long,
    val iconName: String,
    val itemCount: Int
)

data class StorageInfo(
    val totalStorageBytes: Long,
    val freeStorageBytes: Long,
    val usedStorageBytes: Long,
    val usagePercentage: Float,
    val categories: List<StorageCategoryInfo>
)

data class BatteryInfo(
    val percentage: Int,
    val isCharging: Boolean,
    val chargingSource: String, // AC, USB, Wireless, Battery
    val temperatureCelsius: Float,
    val voltageMv: Int,
    val health: String,
    val technology: String
)

data class InstalledAppItem(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val targetSdkVersion: Int,
    val isSystemApp: Boolean,
    val permissionsCount: Int,
    val dangerousPermissions: List<String>,
    val riskLevel: RiskLevel,
    val riskReason: String
)

enum class RiskLevel {
    SAFE,
    MODERATE,
    HIGH
}

data class AppCacheItem(
    val packageName: String,
    val appName: String,
    val cacheSizeBytes: Long,
    val isInstalledOnDevice: Boolean,
    val categoryLabel: String,
    var isCleaned: Boolean = false,
    var isSelected: Boolean = true
)

data class VirusItem(
    val id: String,
    val name: String,
    val threatType: String, // Adware, Trojan, Spyware, Hidden Payload, Suspicious Tracker
    val locationPath: String,
    val riskLevel: RiskLevel,
    val threatDescription: String,
    var isCleaned: Boolean = false
)

data class CleanableItem(
    val id: String,
    val title: String,
    val description: String,
    val sizeBytes: Long,
    val category: CleanCategory,
    var isSelected: Boolean = true
)

enum class CleanCategory {
    APP_CACHE,
    TEMP_FILES,
    LOG_FILES,
    RESIDUAL_FILES,
    DOWNLOADS,
    EMPTY_FOLDERS
}

data class LargeFileItem(
    val name: String,
    val path: String,
    val sizeBytes: Long,
    val type: String,
    val lastModified: Long,
    var isSelected: Boolean = false
)

data class CleaningResult(
    val cleanedBytes: Long,
    val itemsCleanedCount: Int,
    val durationMs: Long,
    val details: List<String>
)

data class OptimizationHistory(
    val id: Long,
    val timestamp: Long,
    val type: String, // Cleaner, RAM, Storage, Battery, Virus
    val amountFormatted: String,
    val description: String
)
