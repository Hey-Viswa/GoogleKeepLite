package com.optivus.googlekeeplite.presentation.settings

/**
 * Represents the different note display styles available
 */
enum class NoteStyle {
    DEFAULT,
    ROUNDED,
    MATERIAL,
    MINIMAL
}

/**
 * Represents the different grid layout options
 */
enum class GridLayout {
    ADAPTIVE,    // Default adaptive grid based on screen size
    ONE_COLUMN,  // Single column of notes
    TWO_COLUMNS, // Two columns of notes
    THREE_COLUMNS // Three columns of notes (for larger screens)
}

/**
 * State class for application settings
 */
data class SettingsState(
    val noteStyle: NoteStyle = NoteStyle.DEFAULT,
    val gridLayout: GridLayout = GridLayout.ADAPTIVE,
    val enableAnimations: Boolean = true,
    val darkModeEnabled: Boolean = false
)
