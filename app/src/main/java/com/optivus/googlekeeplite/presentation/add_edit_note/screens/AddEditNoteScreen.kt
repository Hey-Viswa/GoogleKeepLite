package com.optivus.googlekeeplite.presentation.add_edit_note.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.optivus.googlekeeplite.presentation.add_edit_note.AddEditNoteEvent
import com.optivus.googlekeeplite.presentation.add_edit_note.UiEvent
import com.optivus.googlekeeplite.presentation.add_edit_note.viewmodel.AddEditNoteViewModel
import com.optivus.googlekeeplite.presentation.colorpicker.ColorPickerSection
import com.optivus.googlekeeplite.presentation.components.TransparentHintTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    noteId: Int
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showColorPicker by remember { mutableStateOf(false) }

    // Define note background colors (same as in NoteItem)
    val colors = listOf(
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

    val backgroundColor = colors.getOrNull(state.color) ?: Color.White

    // Handle UI events
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }

                is UiEvent.SaveNote -> {
                    onNavigateBack()
                }

                UiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showColorPicker = !showColorPicker }) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Change color"
                        )
                    }

                    IconButton(onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save note"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TransparentHintTextField(
                    text = state.title,
                    hint = "Title",
                    onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    isHintVisible = state.title.isBlank(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TransparentHintTextField(
                    text = state.content,
                    hint = "Enter note content here...",
                    onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    isHintVisible = state.content.isBlank(),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    singleLine = false,
                    maxLines = Int.MAX_VALUE
                )

                AnimatedVisibility(
                    visible = showColorPicker,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ColorPickerSection(
                        selectedColorIndex = state.color,
                        onColorSelected = { colorIndex ->
                            viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorIndex))
                            showColorPicker = false
                        }
                    )
                }
            }
        }
    }
}