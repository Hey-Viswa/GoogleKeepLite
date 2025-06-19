package com.optivus.googlekeeplite.presentation.settings

/**
 * Repository interface for managing app settings
 */
interface SettingsRepository {
    suspend fun getNoteStyle(): NoteStyle
    suspend fun setNoteStyle(style: NoteStyle)

    suspend fun getGridLayout(): GridLayout
    suspend fun setGridLayout(layout: GridLayout)

    suspend fun getEnableAnimations(): Boolean
    suspend fun setEnableAnimations(enabled: Boolean)

    suspend fun getDarkMode(): Boolean
    suspend fun setDarkMode(enabled: Boolean)
}
