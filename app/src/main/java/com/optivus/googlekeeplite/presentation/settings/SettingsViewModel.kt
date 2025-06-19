package com.optivus.googlekeeplite.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            // Load settings from repository
            val noteStyle = settingsRepository.getNoteStyle()
            val gridLayout = settingsRepository.getGridLayout()
            val enableAnimations = settingsRepository.getEnableAnimations()
            val darkMode = settingsRepository.getDarkMode()

            // Update state with loaded settings
            _settingsState.update {
                it.copy(
                    noteStyle = noteStyle,
                    gridLayout = gridLayout,
                    enableAnimations = enableAnimations,
                    darkModeEnabled = darkMode
                )
            }
        }
    }

    fun updateNoteStyle(style: NoteStyle) {
        viewModelScope.launch {
            settingsRepository.setNoteStyle(style)
            loadSettings()
        }
    }

    fun updateGridLayout(layout: GridLayout) {
        viewModelScope.launch {
            settingsRepository.setGridLayout(layout)
            loadSettings()
        }
    }

    fun updateEnableAnimations(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setEnableAnimations(enabled)
            loadSettings()
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
            loadSettings()
        }
    }
}
