package com.optivus.googlekeeplite.presentation.utils

import androidx.compose.ui.graphics.Color

/**
 * Shared color definitions for consistent appearance across the app
 */
object ColorUtils {
    val noteColors = listOf(
        Color.White,
        Color(0xFFF28B82), // Red
        Color(0xFFFBBC04), // Orange
        Color(0xFFFFF475), // Yellow
        Color(0xFFCBFF90), // Green
        Color(0xFFA7FFEB), // Teal
        Color(0xFFCBF0F8), // Blue
        Color(0xFFAECBFA), // Dark Blue
        Color(0xFFD7AEFB), // Purple
        Color(0xFFFDCFE8), // Pink
        Color(0xFFE6C9A8), // Brown
        Color(0xFFE8EAED)  // Gray
    )

    val darkNoteColors = listOf(
        Color(0xFF202124), // Dark surface
        Color(0xFF5C2B29), // Dark Red
        Color(0xFF614A19), // Dark Orange
        Color(0xFF635D19), // Dark Yellow
        Color(0xFF345920), // Dark Green
        Color(0xFF16504B), // Dark Teal
        Color(0xFF2C5452), // Dark Blue
        Color(0xFF2E3F5A), // Dark Dark Blue
        Color(0xFF443A5C), // Dark Purple
        Color(0xFF5B3B4D), // Dark Pink
        Color(0xFF4E3829), // Dark Brown
        Color(0xFF3C4043)  // Dark Gray
    )

    /**
     * Get the appropriate note color based on the color index and dark mode state
     */
    fun getNoteColor(colorIndex: Int, isDarkMode: Boolean): Color {
        val colors = if (isDarkMode) darkNoteColors else noteColors
        return if (colorIndex >= 0 && colorIndex < colors.size) colors[colorIndex] else colors[0]
    }
}
