package com.example

import com.example.model.CleanCategory
import com.example.model.CleanableItem
import com.example.util.FormatUtils
import org.junit.Assert.*
import org.junit.Test

class CleanerUnitTest {

    @Test
    fun formatFileSize_formatsCorrectly() {
        assertEquals("0 B", FormatUtils.formatFileSize(0L))
        assertEquals("512 B", FormatUtils.formatFileSize(512L))
        assertEquals("1 KB", FormatUtils.formatFileSize(1024L))
        assertEquals("1.5 MB", FormatUtils.formatFileSize((1.5 * 1024 * 1024).toLong()))
        assertEquals("4 GB", FormatUtils.formatFileSize(4L * 1024 * 1024 * 1024))
    }

    @Test
    fun formatMemorySize_formatsCorrectly() {
        assertEquals("8.0 GB", FormatUtils.formatMemorySize(8L * 1024 * 1024 * 1024))
        assertEquals("512 MB", FormatUtils.formatMemorySize(512L * 1024 * 1024))
    }

    @Test
    fun cleanableItem_toggleSelection() {
        val item = CleanableItem(
            id = "cache",
            title = "App Cache",
            description = "Test description",
            sizeBytes = 1048576L,
            category = CleanCategory.APP_CACHE,
            isSelected = true
        )
        assertTrue(item.isSelected)
        item.isSelected = false
        assertFalse(item.isSelected)
    }
}
