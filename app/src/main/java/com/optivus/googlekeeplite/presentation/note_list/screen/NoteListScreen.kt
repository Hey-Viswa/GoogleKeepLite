package com.optivus.googlekeeplite.presentation.note_list.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.optivus.googlekeeplite.presentation.components.NoteDialog
import com.optivus.googlekeeplite.presentation.components.NoteItem
import com.optivus.googlekeeplite.presentation.components.NoteItemCompact
import com.optivus.googlekeeplite.presentation.note_list.NoteListEvent
import com.optivus.googlekeeplite.presentation.note_list.UiEvent
import com.optivus.googlekeeplite.presentation.note_list.viewmodel.NoteListViewModel
import com.optivus.googlekeeplite.presentation.utils.ColorUtils
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    onNavigateToEditNote: (Int) -> Unit,
    onNavigateToSettings: () -> Unit = {},
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Get settings preferences
    val settingsViewModel: com.optivus.googlekeeplite.presentation.settings.SettingsViewModel =
        hiltViewModel()
    val settingsState = settingsViewModel.settingsState.collectAsState().value

    // Apply dark mode setting (no longer trying to access private theme properties)
    val isDarkTheme = settingsState.darkModeEnabled || isSystemInDarkTheme()

    // Determine if animations should be enabled
    val enableAnimations = settingsState.enableAnimations

    // handle delete note events
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        // User clicked the Undo button
                        viewModel.onEvent(NoteListEvent.OnRestoreNote)
                    }
                }
            }
        }
    }

    // Observe search query changes
    LaunchedEffect(searchQuery) {
        viewModel.onEvent(NoteListEvent.OnSearchQueryChange(searchQuery))
    }

    if (showAddNoteDialog) {
        NoteDialog(
            show = showAddNoteDialog,
            note = null,
            onSave = { note ->
                viewModel.onEvent(NoteListEvent.OnAddNote(note))
                showAddNoteDialog = false
            },
            onDismiss = {
                showAddNoteDialog = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isSearchActive) {
                Column {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { searchQuery = it },
                        active = true,
                        onActiveChange = { isSearchActive = it },
                        placeholder = { Text("Search notes") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.onEvent(NoteListEvent.OnClearSearch)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Filter options in the expanded search area
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Filter by color",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Color filter chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                // Add "All Colors" option
                                item {
                                    FilterChip(
                                        selected = state.filterByColor == null,
                                        onClick = { viewModel.onEvent(NoteListEvent.OnFilterByColor(null)) },
                                        label = { Text("All") },
                                        leadingIcon = if (state.filterByColor == null) {
                                            {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        } else null
                                    )
                                }

                                // Add color options
                                items(ColorUtils.noteColors.size) { colorIndex ->
                                    val color = ColorUtils.noteColors[colorIndex]
                                    FilterChip(
                                        selected = state.filterByColor == colorIndex,
                                        onClick = {
                                            viewModel.onEvent(
                                                NoteListEvent.OnFilterByColor(
                                                    colorIndex
                                                )
                                            )
                                        },
                                        label = { Text("") },
                                        leadingIcon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(color, CircleShape)
                                                    .then(
                                                        if (state.filterByColor == colorIndex) {
                                                            Modifier.border(
                                                                2.dp,
                                                                MaterialTheme.colorScheme.primary,
                                                                CircleShape
                                                            )
                                                        } else {
                                                            Modifier
                                                        }
                                                    )
                                            )
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Sort options
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sort by date:",
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                FilterChip(
                                    selected = !state.sortByDateAscending,
                                    onClick = { viewModel.onEvent(NoteListEvent.OnToggleSortOrder(false)) },
                                    label = { Text("Newest first") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                FilterChip(
                                    selected = state.sortByDateAscending,
                                    onClick = { viewModel.onEvent(NoteListEvent.OnToggleSortOrder(true)) },
                                    label = { Text("Oldest first") }
                                )
                            }

                            // Clear filters button
                            TextButton(
                                onClick = { viewModel.onEvent(NoteListEvent.OnClearFilters) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear filters"
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear filters")
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            // Search results section
                            Text(
                                text = "Search results",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Loading indicator
                            if (state.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }

                            // Search results
                            if (state.notes.isNotEmpty() && !state.isLoading) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(320.dp)
                                ) {
                                    items(
                                        items = state.notes,
                                        key = { it.id ?: it.hashCode() }
                                    ) { note ->
                                        NoteItemCompact(
                                            note = note,
                                            onNoteClick = {
                                                isSearchActive = false
                                                onNavigateToEditNote(note.id ?: -1)
                                            }
                                        )
                                    }
                                }
                            }
                            // No search results message
                            else if (!state.isLoading && state.searchQuery.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No notes found for '${state.searchQuery}'",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                TopAppBar(
                    title = { Text("Google Keep Lite") },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        IconButton(onClick = {
                            isSearchActive = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }

                        // Menu icon with anchored dropdown
                        Box {
                            IconButton(onClick = {
                                isMenuExpanded = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )
                            }

                            // Settings menu anchored to the icon
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        isMenuExpanded = false
                                        onNavigateToSettings()
                                    }
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddNoteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Add Note"
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null && state.error!!.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error!!, color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                state.notes.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No notes yet", style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap + to create one",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    AnimatedVisibility(
                        visible = true,
                        // Only apply animations if they're enabled in settings
                        enter = if (enableAnimations) {
                            fadeIn(animationSpec = tween(durationMillis = 150)) +
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                        } else {
                            fadeIn(animationSpec = tween(durationMillis = 50))
                        }
                    ) {
                        // Apply grid layout based on settings
                        val columns = when (settingsState.gridLayout) {
                            com.optivus.googlekeeplite.presentation.settings.GridLayout.ONE_COLUMN ->
                                StaggeredGridCells.Fixed(1)
                            com.optivus.googlekeeplite.presentation.settings.GridLayout.TWO_COLUMNS ->
                                StaggeredGridCells.Fixed(2)
                            com.optivus.googlekeeplite.presentation.settings.GridLayout.THREE_COLUMNS ->
                                StaggeredGridCells.Fixed(3)
                            else ->
                                StaggeredGridCells.Adaptive(minSize = 160.dp)
                        }

                        LazyVerticalStaggeredGrid(
                            columns = columns,
                            state = gridState,
                            contentPadding = PaddingValues(12.dp),
                            verticalItemSpacing = 12.dp,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.notes,
                                key = { it.id ?: it.hashCode() }
                            ) { note ->
                                NoteItem(
                                    note = note,
                                    onNoteClick = { onNavigateToEditNote(note.id ?: -1) },
                                    onDeleteClick = {
                                        viewModel.onEvent(NoteListEvent.OnDeleteNote(note))
                                    },
                                    noteStyle = settingsState.noteStyle,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
