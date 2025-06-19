# GoogleKeepLite

A lightweight Android note-taking application inspired by Google Keep, built using modern Android development practices including Jetpack Compose, MVVM architecture, and clean architecture principles.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech Stack & Architecture](#tech-stack--architecture)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Usage](#usage)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

## Overview

GoogleKeepLite is a simplified version of Google Keep that allows users to create, edit, delete, and organize notes. The app features a clean, Material Design 3 UI with various customization options including different note colors, edge-to-edge design, and intuitive navigation.

## Features

- **Note Management**
  - Create, read, update, and delete notes
  - Color-code notes for organization
  - View notes in a grid layout

- **User Interface**
  - Modern Material 3 design
  - Responsive edge-to-edge UI
  - Intuitive navigation between screens
  - Comfortable reading and editing experience

- **Architecture & Performance**
  - Clean architecture with separation of concerns
  - MVVM pattern for maintainable code
  - Efficient data handling with Room database
  - Dependency injection with Hilt

## Screenshots

*[Add screenshots here]*

## Tech Stack & Architecture

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit for building native Android UI
- **Coroutines & Flow** - Asynchronous programming
- **Room Database** - Local data persistence
- **Hilt** - Dependency injection
- **Navigation Compose** - Type-safe navigation between screens
- **MVVM Architecture** - Separation of concerns with ViewModel pattern

### Key Dependencies
```kotlin
// Core Android & UI
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.ui)
implementation(libs.androidx.material3)
implementation(libs.androidx.material.icons.extended)

// Architecture Components
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.navigation.compose)

// Database
implementation(libs.androidx.room.runtime)
ksp(libs.androidx.room.compiler)

// Dependency Injection
implementation(libs.hilt.android)
ksp(libs.hilt.android.compiler)
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// UI Enhancements
implementation(libs.accompanist.systemuicontroller)
```

## Project Structure

The app follows Clean Architecture principles:

```
app/
├── data/               # Data layer (Room, repositories)
├── di/                 # Dependency Injection modules
├── domain/             # Domain layer (models, use cases)
│   └── model/          # Data models (Note)
└── presentation/       # UI layer (screens, viewmodels)
    ├── add_edit_note/  # Add/Edit note screen
    ├── components/     # Reusable UI components
    ├── navigation/     # Navigation setup
    ├── note_list/      # Note list screen
    └── settings/       # Settings screen
```

## Setup & Installation

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files and build the project
4. Run on an emulator or physical device (minimum SDK 24)

## Usage

- **Home Screen**: View all your notes in a grid layout
- **Create Note**: Tap the floating action button to create a new note
- **Edit Note**: Tap on any note to edit its content
- **Delete Note**: Use the delete icon to remove a note
- **Settings**: Access app settings to customize your experience

## Future Improvements

- Note categories/labels
- Rich text formatting
- Image attachments
- Cloud synchronization
- Reminder functionality
- Archive and trash features
- Collaborative notes

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

[Add your license information here]
