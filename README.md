# GoogleKeepLite

A lightweight Android note-taking application inspired by Google Keep, built using modern Android development practices including Jetpack Compose, MVVM architecture, and clean architecture principles.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [Architecture](#architecture)
- [Implementation Guide](#implementation-guide)
  - [Setting up the Project](#setting-up-the-project)
  - [Notes CRUD Operations](#notes-crud-operations)
  - [Search Functionality](#search-functionality)
  - [Settings & Preferences](#settings--preferences)
  - [UI Components & Animations](#ui-components--animations)
- [Future Improvements](#future-improvements)

## Overview

GoogleKeepLite is a simplified version of Google Keep that allows users to create, edit, delete, and organize notes. The app features a clean, Material Design 3 UI with various customization options including different note styles, grid layouts, and theme preferences.

## Features

- **Note Management**
  - Create, read, update, and delete notes
  - Color-code notes for organization
  - View notes in a customizable grid layout

- **Search & Filtering**
  - Search notes by title or content
  - Filter notes by color
  - Sort notes by date (newest/oldest first)

- **UI Customization**
  - Multiple note display styles (Default, Rounded, Material, Minimal)
  - Adjustable grid layout (Adaptive, Single Column, Two Columns, Three Columns)
  - Toggle animations on/off
  - Dark mode support

- **User Experience**
  - Smooth animations and transitions
  - Undo deleted notes
  - Fast, responsive interface

## Tech Stack & Dependencies

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit for building native Android UI
- **Coroutines & Flow** - Asynchronous programming
- **Room Database** - Local data persistence
- **DataStore Preferences** - User settings storage
- **Hilt** - Dependency injection
- **MVVM Architecture** - Separation of concerns with ViewModel pattern

### Dependencies
```kotlin
// Core Android dependencies
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)

// Compose UI
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.graphics)
implementation(libs.androidx.compose.ui.tooling.preview)
implementation(libs.androidx.compose.material3)

// Navigation
implementation(libs.androidx.navigation.compose)

// Room Database
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)

// DataStore Preferences
implementation(libs.androidx.datastore.preferences)

// Hilt for dependency injection
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)
implementation(libs.androidx.hilt.navigation.compose)

// Lifecycle & ViewModel
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.androidx.lifecycle.runtime.compose)
```

## Architecture

GoogleKeepLite follows Clean Architecture principles with MVVM pattern:

```
app/
├── data/
│   ├── local/           # Room Database and DAOs
│   └── repository/      # Repository implementations
├── di/                  # Dependency Injection modules
├── domain/
│   ├── model/           # Business models
│   ├── repository/      # Repository interfaces
│   ├── usecases/        # Use case implementations
│   └── util/            # Domain utilities
├── presentation/
│   ├── add_edit_note/   # Note editing screen
│   ├── components/      # Reusable UI components
│   ├── navigation/      # Navigation setup
│   ├── note_list/       # Main note list screen
│   ├── settings/        # Settings screen and repository
│   └── ui/              # Theme and UI utilities
└── MainActivity.kt      # Entry point
```

## Implementation Guide

### Setting up the Project

1. **Project Setup**
   - Create a new Android project with Jetpack Compose
   - Configure build.gradle files with necessary dependencies
   - Set up Hilt for dependency injection

2. **Database Setup**
   ```kotlin
   // Define Note entity
   @Entity(tableName = "note_table")
   data class Note(
       @PrimaryKey(autoGenerate = true) val id: Int? = null,
       val title: String? = null,
       val content: String? = null,
       val timestamp: Long,
       val color: Int = 0
   )
   
   // Create DAO interface
   @Dao
   interface NoteDao {
       @Query("SELECT * FROM note_table")
       fun getAllNotes(): Flow<List<Note>>
       
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertNote(note: Note)
       
       // Other CRUD operations
   }
   
   // Create Database class
   @Database(entities = [Note::class], version = 1)
   abstract class NoteDatabase : RoomDatabase() {
       abstract val noteDao: NoteDao
   }
   ```

3. **Repository Implementation**
   ```kotlin
   // Define repository interface
   interface NoteRepository {
       fun getAllNotes(): Flow<List<Note>>
       suspend fun getNoteById(id: Int): Note?
       suspend fun insertNote(note: Note)
       suspend fun deleteNote(note: Note)
       // Other methods
   }
   
   // Implement repository
   class NoteRepositoryImpl(
       private val noteDao: NoteDao
   ) : NoteRepository {
       override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
       
       // Other implementation methods
   }
   ```

### Notes CRUD Operations

1. **Define Use Cases**
   ```kotlin
   // Example: AddNoteUseCase
   class AddNoteUseCase(
       private val repository: NoteRepository
   ) {
       suspend operator fun invoke(note: Note) {
           repository.insertNote(note)
       }
   }
   
   // Bundle use cases for injection
   data class NoteUseCases(
       val getNotes: GetNotesUseCase,
       val deleteNote: DeleteNoteUseCase,
       val addNote: AddNoteUseCase,
       val getNoteById: GetNoteByIdUseCase,
       val updateNote: UpdateNoteUseCase,
       val searchNotes: SearchNotesUseCase,
       val getNotesCount: GetNotesCountUseCase
   )
   ```

2. **ViewModel Implementation**
   ```kotlin
   @HiltViewModel
   class NoteListViewModel @Inject constructor(
       private val noteUseCase: NoteUseCases
   ) : ViewModel() {
       private val _state = MutableStateFlow(NoteListState())
       val state: StateFlow<NoteListState> = _state.asStateFlow()
       
       // Event handling
       fun onEvent(event: NoteListEvent) {
           when (event) {
               is NoteListEvent.OnDeleteNote -> {
                   viewModelScope.launch {
                       // Handle note deletion
                   }
               }
               // Other events
           }
       }
       
       // Loading notes
       private fun loadNotes() {
           viewModelScope.launch {
               noteUseCase.getNotes().collect { notes ->
                   _state.update { it.copy(notes = notes, isLoading = false) }
               }
           }
       }
   }
   ```

3. **UI Implementation**
   ```kotlin
   @Composable
   fun NoteListScreen(
       onNavigateToEditNote: (Int) -> Unit,
       viewModel: NoteListViewModel = hiltViewModel()
   ) {
       val state by viewModel.state.collectAsStateWithLifecycle()
       
       // UI implementation with LazyVerticalStaggeredGrid for adaptive layout
   }
   ```

### Search Functionality

1. **Search Use Case**
   ```kotlin
   class SearchNotesUseCase(
       private val repository: NoteRepository
   ) {
       suspend operator fun invoke(
           query: String,
           filterByColor: Int? = null,
           sortByDate: Boolean = true,
           ascending: Boolean = false
       ): List<Note> {
           // Implementation of search with filtering and sorting
       }
   }
   ```

2. **ViewModel Search Functions**
   ```kotlin
   private fun filterAndSortNotes() {
       viewModelScope.launch {
           // Get search parameters from state
           val query = _state.value.searchQuery
           val filterByColor = _state.value.filterByColor
           val ascending = _state.value.sortByDateAscending
           
           // Get notes and apply filters
           noteUseCase.getNotes().collect { allNotes ->
               var filteredNotes = if (query.isBlank()) {
                   allNotes
               } else {
                   // Filter by query
               }
               
               // Filter by color
               if (filterByColor != null) {
                   filteredNotes = filteredNotes.filter { it.color == filterByColor }
               }
               
               // Sort notes based on NoteOrder
               filteredNotes = when (_state.value.noteOrder) {
                   is NoteOrder.DateAsc -> filteredNotes.sortedBy { it.timestamp }
                   is NoteOrder.DateDesc -> filteredNotes.sortedByDescending { it.timestamp }
                   is NoteOrder.Color -> filteredNotes.sortedBy { it.color }
               }
               
               // Update UI state with filtered notes
           }
       }
   }
   ```

3. **Search UI Implementation**
   ```kotlin
   @Composable
   fun SearchBar(
       query: String,
       onQueryChange: (String) -> Unit,
       // Other parameters
   ) {
       SearchBar(
           query = query,
           onQueryChange = onQueryChange,
           onSearch = { /* Handle search */ },
           active = isSearchActive,
           onActiveChange = { isSearchActive = it },
           // Other UI elements
       ) {
           // Search filters UI
           Column {
               // Color filters
               LazyRow {
                   // Color filter chips
               }
               
               // Sort options
               Row {
                   // Sort buttons
               }
           }
       }
   }
   ```

### Settings & Preferences

1. **Define Settings Types**
   ```kotlin
   enum class NoteStyle {
       DEFAULT, ROUNDED, MATERIAL, MINIMAL
   }
   
   enum class GridLayout {
       ADAPTIVE, ONE_COLUMN, TWO_COLUMNS, THREE_COLUMNS
   }
   ```

2. **Settings Repository**
   ```kotlin
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
       
       // Implementation of get/set methods using DataStore
   }
   ```

3. **Settings ViewModel**
   ```kotlin
   @HiltViewModel
   class SettingsViewModel @Inject constructor(
       private val settingsRepository: SettingsRepository
   ) : ViewModel() {
       private val _settingsState = MutableStateFlow(SettingsState())
       val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()
       
       // Methods to update settings
       fun updateNoteStyle(style: NoteStyle) {
           viewModelScope.launch {
               settingsRepository.setNoteStyle(style)
               loadSettings()
           }
       }
       
       // Other update methods
   }
   ```

4. **Settings UI**
   ```kotlin
   @Composable
   fun SettingsScreen(
       settingsState: SettingsState,
       onNoteStyleSelected: (NoteStyle) -> Unit,
       onGridLayoutSelected: (GridLayout) -> Unit,
       onToggleAnimations: (Boolean) -> Unit,
       onToggleDarkMode: (Boolean) -> Unit,
       onBack: () -> Unit
   ) {
       // Settings UI implementation
   }
   ```

### UI Components & Animations

1. **Note Item Component**
   ```kotlin
   @Composable
   fun NoteItem(
       note: Note,
       onNoteClick: () -> Unit,
       onDeleteClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       // Implementation of a note card with animations
   }
   ```

2. **Animation Implementation**
   ```kotlin
   // Grid Animation
   AnimatedVisibility(
       visible = true, 
       enter = fadeIn(animationSpec = tween(durationMillis = 150)) + 
               slideInVertically(
                  initialOffsetY = { it }, 
                  animationSpec = spring(
                      dampingRatio = Spring.DampingRatioMediumBouncy,
                      stiffness = Spring.StiffnessLow
                  )
               )
   ) {
       // Grid content
   }
   
   // Dialog Animation
   AnimatedVisibility(
       visible = show,
       enter = fadeIn(animationSpec = tween(durationMillis = 150)) + 
               slideInVertically(
                   initialOffsetY = { it / 2 },
                   animationSpec = spring(
                       dampingRatio = Spring.DampingRatioLowBouncy,
                       stiffness = Spring.StiffnessMediumLow
                   )
               ),
       exit = fadeOut(animationSpec = tween(durationMillis = 100)) + 
              slideOutVertically(
                  targetOffsetY = { it / 2 },
                  animationSpec = tween(durationMillis = 150)
              )
   ) {
       // Dialog content
   }
   ```

3. **Navigation Setup**
   ```kotlin
   @Composable
   fun Navigation() {
       val navController = rememberNavController()
       
       NavHost(
           navController = navController,
           startDestination = Screen.NoteList.route
       ) {
           // Route definitions
           composable(route = Screen.NoteList.route) {
               // NoteListScreen
           }
           
           composable(
               route = Screen.AddEditNote.route + "?noteId={noteId}",
               arguments = listOf(
                   navArgument("noteId") {
                       type = NavType.IntType
                       defaultValue = -1
                   }
               )
           ) { 
               // AddEditNoteScreen
           }
           
           composable(route = Screen.Settings.route) {
               // SettingsScreen
           }
       }
   }
   ```

## Future Improvements

- **Note Categories/Labels**: Add ability to organize notes with labels
- **Cloud Sync**: Synchronize notes across devices
- **Rich Text Formatting**: Support for formatting text in notes
- **Attachments**: Allow images and other file attachments
- **Reminders/Notifications**: Add reminder functionality to notes
- **Collaborative Notes**: Share and collaborate on notes with others
- **Export/Import Notes**: Backup and restore notes in various formats
- **Widgets**: Home screen widgets for quick note access
- **Biometric Security**: Add option to lock sensitive notes

---

*This project was developed as a learning exercise to demonstrate modern Android development practices and architecture principles.*
