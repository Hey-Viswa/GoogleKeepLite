package com.optivus.googlekeeplite.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsState: SettingsState,
    onNoteStyleSelected: (NoteStyle) -> Unit,
    onGridLayoutSelected: (GridLayout) -> Unit,
    onToggleAnimations: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Note Style Section
            SettingSection(title = "Note Display Style") {
                Column(modifier = Modifier.selectableGroup()) {
                    NoteStyle.values().forEach { style ->
                        val styleLabel = when (style) {
                            NoteStyle.DEFAULT -> "Default"
                            NoteStyle.ROUNDED -> "Rounded"
                            NoteStyle.MATERIAL -> "Material"
                            NoteStyle.MINIMAL -> "Minimal"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = settingsState.noteStyle == style,
                                    onClick = { onNoteStyleSelected(style) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settingsState.noteStyle == style,
                                onClick = null // Handled by the selectable modifier
                            )
                            Text(
                                text = styleLabel,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

            // Grid Layout Section
            SettingSection(title = "Grid Layout") {
                Column(modifier = Modifier.selectableGroup()) {
                    GridLayout.values().forEach { layout ->
                        val layoutLabel = when (layout) {
                            GridLayout.ADAPTIVE -> "Adaptive (Default)"
                            GridLayout.ONE_COLUMN -> "Single Column"
                            GridLayout.TWO_COLUMNS -> "Two Columns"
                            GridLayout.THREE_COLUMNS -> "Three Columns"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = settingsState.gridLayout == layout,
                                    onClick = { onGridLayoutSelected(layout) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settingsState.gridLayout == layout,
                                onClick = null // Handled by the selectable modifier
                            )
                            Text(
                                text = layoutLabel,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

            // Other Settings Section
            SettingSection(title = "Additional Settings") {
                // Animations toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Enable Animations",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = settingsState.enableAnimations,
                        onCheckedChange = onToggleAnimations
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Dark Mode toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = settingsState.darkModeEnabled,
                        onCheckedChange = onToggleDarkMode
                    )
                }
            }
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}
