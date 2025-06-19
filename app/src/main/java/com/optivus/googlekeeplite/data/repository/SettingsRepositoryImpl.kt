package com.optivus.googlekeeplite.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.optivus.googlekeeplite.presentation.settings.GridLayout
import com.optivus.googlekeeplite.presentation.settings.NoteStyle
import com.optivus.googlekeeplite.presentation.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SettingsRepository {

    private object PreferenceKeys {
        val NOTE_STYLE = intPreferencesKey("note_style")
        val GRID_LAYOUT = intPreferencesKey("grid_layout")
        val ENABLE_ANIMATIONS = booleanPreferencesKey("enable_animations")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    override suspend fun getNoteStyle(): NoteStyle {
        return context.dataStore.data.map { preferences ->
            val styleOrdinal = preferences[PreferenceKeys.NOTE_STYLE] ?: 0
            NoteStyle.entries.getOrElse(styleOrdinal) { NoteStyle.DEFAULT }
        }.first()
    }

    override suspend fun setNoteStyle(style: NoteStyle) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.NOTE_STYLE] = style.ordinal
        }
    }

    override suspend fun getGridLayout(): GridLayout {
        return context.dataStore.data.map { preferences ->
            val layoutOrdinal = preferences[PreferenceKeys.GRID_LAYOUT] ?: 0
            GridLayout.entries.getOrElse(layoutOrdinal) { GridLayout.ADAPTIVE }
        }.first()
    }

    override suspend fun setGridLayout(layout: GridLayout) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.GRID_LAYOUT] = layout.ordinal
        }
    }

    override suspend fun getEnableAnimations(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.ENABLE_ANIMATIONS] ?: true
        }.first()
    }

    override suspend fun setEnableAnimations(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ENABLE_ANIMATIONS] = enabled
        }
    }

    override suspend fun getDarkMode(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DARK_MODE] ?: false
        }.first()
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DARK_MODE] = enabled
        }
    }
}
