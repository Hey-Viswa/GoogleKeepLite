package com.optivus.googlekeeplite.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.optivus.googlekeeplite.presentation.add_edit_note.screens.AddEditNoteScreen
import com.optivus.googlekeeplite.presentation.note_list.screen.NoteListScreen
import com.optivus.googlekeeplite.presentation.settings.SettingsScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.optivus.googlekeeplite.presentation.settings.SettingsViewModel


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        // Note list screen
        composable(route = Screen.NoteList.route) {
            NoteListScreen(
                onNavigateToEditNote = { noteId ->
                    navController.navigate(
                        Screen.AddEditNote.route + "?noteId=$noteId"
                    )
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.AddEditNote.route + "?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1

            AddEditNoteScreen(
                noteId = noteId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        // Settings screen
        composable(route = Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState = settingsViewModel.settingsState.collectAsState().value

            SettingsScreen(
                settingsState = settingsState,
                onNoteStyleSelected = { style ->
                    settingsViewModel.updateNoteStyle(style)
                },
                onGridLayoutSelected = { layout ->
                    settingsViewModel.updateGridLayout(layout)
                },
                onToggleAnimations = { enabled ->
                    settingsViewModel.updateEnableAnimations(enabled)
                },
                onToggleDarkMode = { enabled ->
                    settingsViewModel.updateDarkMode(enabled)
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}