# GoogleKeepLite

A lightweight Android note-taking application inspired by Google Keep, built using modern Android development practices including Jetpack Compose, MVVM architecture, and clean architecture principles.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
  - [Note Management](#note-management)
  - [User Interface](#user-interface)
  - [Search & Filtering](#search--filtering)
  - [Settings & Customization](#settings--customization)
- [Screenshots](#screenshots)
- [Tech Stack & Architecture](#tech-stack--architecture)
  - [Core Technologies](#core-technologies)
  - [Key Dependencies](#key-dependencies)
  - [Architecture Patterns](#architecture-patterns)
- [Project Structure](#project-structure)
- [Implementation Details](#implementation-details)
  - [Data Management](#data-management)
  - [UI Implementation](#ui-implementation)
  - [State Management](#state-management)
  - [Navigation](#navigation)
  - [Dependency Injection](#dependency-injection)
- [Setup & Installation](#setup--installation)
- [Usage Guide](#usage-guide)
- [Core Concepts Explained](#core-concepts-explained)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

## Overview

GoogleKeepLite is a simplified version of Google Keep that allows users to create, edit, delete, and organize notes. The app features a clean, Material Design 3 UI with various customization options including different note colors, edge-to-edge design, and intuitive navigation. It demonstrates modern Android development practices and architectural patterns.

## Features

### Note Management
- **Create Notes**: Add new notes with title and content
- **Edit Notes**: Modify existing notes with real-time updates
- **Delete Notes**: Remove unwanted notes with undo functionality
- **Color Coding**: Assign different colors to notes for visual organization
  - Multiple color options available through a color picker
  - Consistent color representation across the app
- **Timestamps**: Automatic tracking of when notes were created or modified
- **Note Storage**: Persistent local storage using Room database

### User Interface
- **Modern Material 3 Design**: 
  - Follows latest Material Design guidelines
  - Dynamic theming with Material 3 color system
  - Responsive layout that adapts to different screen sizes
- **Edge-to-Edge Design**: 
  - Full-screen layout that maximizes content area
  - System UI integration with proper insets handling
- **Intuitive Navigation**: 
  - Clear navigation between screens using Navigation Compose
  - Smooth transitions and animations between screens
  - Contextual actions based on current screen
- **Grid Layout**: 
  - Responsive grid layout for notes on the main screen
  - Adapts to screen size and orientation changes

### Search & Filtering
- **Text Search**:
  - Real-time search through note titles and content
  - Highlighted search results for better visibility
  - Search history (optional)
- **Color Filtering**:
  - Filter notes by their assigned color
  - Quick color filter selection through UI
- **Sorting Options**:
  - Sort notes by creation date (newest/oldest)
  - Sort notes by last modified date
  - Sort notes alphabetically

### Settings & Customization
- **Theme Settings**:
  - Light/dark mode toggle
  - System theme following
  - Optional dynamic color support
- **Layout Preferences**:
  - Adjustable grid density
  - Note display options (compact/expanded)
- **Behavior Settings**:
  - Autosave preferences
  - Default note color selection

## Screenshots

*[Add screenshots here]*

## Tech Stack & Architecture

### Core Technologies
- **Kotlin**: Primary programming language with Kotlin features:
  - Coroutines for asynchronous programming
  - Flow for reactive data streams
  - Extension functions for clean API design
  - Data classes for model representation
  
- **Jetpack Compose**: Modern UI toolkit with:
  - Declarative UI design
  - Composable functions for UI components
  - State hoisting and unidirectional data flow
  - Custom animations and transitions
  - LazyLayouts for efficient recycling
  
- **Room Database**: 
  - SQLite abstraction layer
  - Type-safe query building
  - Database schema migrations
  - DAO (Data Access Object) pattern
  - Suspend functions for async operations
  
- **Navigation Compose**: 
  - Type-safe navigation between screens
  - Arguments passing between destinations
  - Deep linking support
  - Back stack management
  
- **Hilt**: 
  - Dependency injection framework
  - Component-based DI architecture
  - Scoped injection
  - ViewModel injection
  - Testing utilities

### Key Dependencies
```kotlin
// Core Android & UI
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.ui.tooling.preview)
implementation(libs.androidx.material3)
implementation(libs.androidx.material.icons.extended)

// Architecture Components
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.navigation.compose)

// Database
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)

// Dependency Injection
implementation(libs.hilt.android)
ksp(libs.hilt.android.compiler)
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Coroutines
implementation(libs.kotlinx.coroutines.core)
implementation(libs.kotlinx.coroutines.android)

// UI Enhancements
implementation(libs.accompanist.systemuicontroller)

// Testing
testImplementation(libs.junit)
androidTestImplementation(libs.androidx.junit)
androidTestImplementation(libs.androidx.espresso.core)
androidTestImplementation(libs.androidx.ui.test.junit4)
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)
```

### Architecture Patterns

The project implements a combination of architectural patterns:

1. **Clean Architecture**: Separation of concerns through layers:
   - **Domain Layer**: Contains business logic and models
   - **Data Layer**: Handles data operations and repository implementations
   - **Presentation Layer**: Manages UI components and ViewModels

2. **MVVM (Model-View-ViewModel)**:
   - **Model**: Domain entities and repository data
   - **View**: Composable UI components
   - **ViewModel**: Handles UI state and business logic

3. **Repository Pattern**:
   - Abstracts data sources behind a consistent API
   - Provides single source of truth for data
   - Handles data caching and retrieval logic

4. **Use Case Pattern**:
   - Encapsulates specific business operations
   - Promotes reusability and testability
   - Provides clear separation of business rules

## Project Structure

```
app/
├── data/                           # Data layer
│   ├── local/                      # Local data sources
│   │   ├── NoteDao.kt              # Room DAO for notes
│   │   ├── NoteDatabase.kt         # Room database configuration
│   │   └── NoteEntity.kt           # Database entity models
│   └── repository/                 # Repository implementations
│       └── NoteRepositoryImpl.kt   # Note repository implementation
├── di/                             # Dependency Injection modules
│   ├── AppModule.kt                # Application-wide providers
│   ├── DatabaseModule.kt           # Database-related providers
│   └── RepositoryModule.kt         # Repository providers
├── domain/                         # Domain layer
│   ├── model/                      # Domain models
│   │   └── Note.kt                 # Core Note model
│   ├── repository/                 # Repository interfaces
│   │   └── NoteRepository.kt       # Note repository interface
│   └── use_case/                   # Business logic use cases
│       ├── AddNoteUseCase.kt       # Add note functionality
│       ├── DeleteNoteUseCase.kt    # Delete note functionality
│       ├── GetNoteUseCase.kt       # Retrieve single note
│       ├── GetNotesUseCase.kt      # Retrieve all notes
│       └── UpdateNoteUseCase.kt    # Update note functionality
└── presentation/                   # Presentation layer
    ├── add_edit_note/              # Add/Edit note screen
    │   ├── components/             # Screen-specific components
    │   ├── AddEditNoteEvent.kt     # UI events for this screen
    │   ├── AddEditNoteState.kt     # UI state for this screen
    │   ├── AddEditNoteViewModel.kt # ViewModel for this screen
    │   └── screens/                # Actual screen composables
    ├── components/                 # Shared UI components
    │   ├── NoteItem.kt             # Individual note component
    │   ├── OrderSection.kt         # Sorting options component
    │   └── SearchBar.kt            # Search component
    ├── navigation/                 # Navigation setup
    │   ├── Navigation.kt           # NavHost configuration
    │   └── Screen.kt               # Route definitions
    ├── note_list/                  # Note list screen
    │   ├── components/             # Screen-specific components
    │   ├── NoteListEvent.kt        # UI events for this screen
    │   ├── NoteListState.kt        # UI state for this screen
    │   ├── NoteListViewModel.kt    # ViewModel for this screen
    │   └── screens/                # Actual screen composables
    └── settings/                   # Settings screen
        ├── SettingsEvent.kt        # UI events for settings
        ├── SettingsState.kt        # UI state for settings
        ├── SettingsViewModel.kt    # Settings ViewModel
        └── SettingsScreen.kt       # Settings screen composable
```

## Implementation Details

### Data Management

#### Note Entity and Room Database
The app uses Room for data persistence:

```kotlin
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int
)

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long
    
    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): Flow<List<NoteEntity>>
}
```

#### Repository Pattern
The repository pattern provides a clean API for data operations:

```kotlin
interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun searchNotes(query: String): Flow<List<Note>>
}

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { notes ->
            notes.map { it.toNote() }
        }
    }
    
    // Other implementations...
}
```

### UI Implementation

#### Composable Components
The UI is built with reusable composable functions:

```kotlin
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onNoteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNoteClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(note.color)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
```

#### Screen Implementation
Screens combine multiple components with state management:

```kotlin
@Composable
fun NoteListScreen(
    onNavigateToEditNote: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    
    // UI implementation with Scaffold, LazyColumn, etc.
    // ...
}
```

### State Management

#### ViewModel State
Each screen has a corresponding state class and ViewModel:

```kotlin
data class NoteListState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val sortOrder: SortOrder = SortOrder.Date(OrderType.Descending),
    val colorFilter: Int? = null
)

class NoteListViewModel(
    private val notesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()
    
    // Event handling, data loading, etc.
    // ...
}
```

#### Event Handling
UI events are processed through sealed classes:

```kotlin
sealed class NoteListEvent {
    data class Search(val query: String) : NoteListEvent()
    data class ToggleSearch(val active: Boolean) : NoteListEvent()
    data class DeleteNote(val note: Note) : NoteListEvent()
    data class SetSortOrder(val sortOrder: SortOrder) : NoteListEvent()
    data class SetColorFilter(val color: Int?) : NoteListEvent()
    object RestoreNote : NoteListEvent()
}
```

### Navigation

The app uses Navigation Compose for screen transitions:

```kotlin
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
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

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
```

### Dependency Injection

The app uses Hilt for dependency injection:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository)
        )
    }
}
```

## Setup & Installation

1. **Prerequisites**:
   - Android Studio (Arctic Fox or newer)
   - Kotlin 1.9.0 or higher
   - JDK 11 or higher
   - Android SDK 34 (target) / 24 (minimum)

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/GoogleKeepLite.git
   cd GoogleKeepLite
   ```

3. **Build and Run**:
   - Open the project in Android Studio
   - Sync Gradle files
   - Build the project (Build > Make Project)
   - Run on an emulator or physical device (minimum SDK 24)

4. **Development Environment Setup**:
   - Enable Jetpack Compose in Android Studio settings
   - Install necessary Android SDK components
   - Configure emulator with appropriate device profile

## Usage Guide

### Main Screen
- **View Notes**: The main screen displays all your notes in a grid layout
- **Create Note**: Tap the floating action button (+ icon) to create a new note
- **Search**: Use the search bar at the top to find notes by title or content
- **Filter**: Tap the filter icon to sort notes or filter by color
- **Settings**: Access the settings menu via the top bar

### Creating/Editing Notes
- **Add Note**: On the add/edit screen, enter a title and content for your note
- **Color Selection**: Change the note color using the color picker at the bottom
- **Save**: Auto-saving occurs when you navigate back (or tap save if implemented)
- **Delete**: Use the delete icon to remove a note

### Search & Filtering
- **Text Search**: Type in the search bar to find matching notes
- **Color Filter**: Select a color from the filter menu to show only notes with that color
- **Sort Options**: Choose from different sorting options (newest first, oldest first, etc.)

### Settings
- **Theme**: Toggle between light and dark mode
- **Grid Layout**: Change the density of the note grid
- **Default Color**: Set the default color for new notes

## Core Concepts Explained

### MVVM Architecture
- **View**: Composable functions that render UI elements
- **ViewModel**: Manages UI state and business logic
- **Model**: Data classes and repositories

The app strictly separates concerns by:
- Having immutable UI state classes
- Processing UI events through the ViewModel
- Using Flow for reactive data updates

### Clean Architecture Layers
- **Presentation Layer**: UI components and ViewModels
- **Domain Layer**: Business logic and use cases
- **Data Layer**: Data source implementations and repositories

This separation ensures:
- Business rules are independent of frameworks
- Dependencies point inward (domain doesn't depend on data or presentation)
- Better testability and maintainability

### Reactive Programming with Flow
The app uses Kotlin Flow for reactive data streams:
- **StateFlow** for UI state management
- **Flow** for database observations
- **SharedFlow** for one-time events (if implemented)

This enables:
- Real-time UI updates
- Efficient data loading
- Reduced boilerplate compared to callbacks

### Jetpack Compose UI Principles
- **Declarative UI**: UI is a function of state
- **Composition**: UI built from small, reusable components
- **Unidirectional Data Flow**: Data flows down, events flow up
- **State Hoisting**: State lifted to appropriate level

### Room Database Best Practices
- **DAOs**: Data Access Objects for clean database access
- **Entities**: Clear data model for database tables
- **Type Converters**: Custom data type handling
- **Suspend Functions**: Non-blocking database operations

## Future Improvements

- **Note Categories/Labels**: 
  - Add ability to categorize notes with labels
  - Filter and sort by labels
  
- **Rich Text Formatting**: 
  - Support for bold, italic, lists, etc.
  - Different text sizes and fonts
  
- **Image Attachments**: 
  - Add images to notes
  - Support for camera and gallery
  
- **Cloud Synchronization**: 
  - Sync notes across devices
  - Google Drive or Firebase integration
  
- **Reminder Functionality**: 
  - Set reminders for notes
  - Notification system
  
- **Archive and Trash**: 
  - Archive notes for later access
  - Trash bin for deleted notes
  
- **Collaborative Notes**: 
  - Share notes with other users
  - Real-time collaboration
  
- **Widgets**: 
  - Home screen widgets
  - Quick note creation

- **Offline-First Architecture**:
  - Improve offline capabilities
  - Better sync conflict resolution

## Contributing

Contributions are welcome! To contribute to this project:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Implement your changes
4. Commit your changes (`git commit -m 'Add some amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

Please ensure:
- Code follows the project's style guidelines
- Tests are added for new functionality
- Documentation is updated as needed

*This project was created as a demonstration of modern Android development practices with Jetpack Compose.*
