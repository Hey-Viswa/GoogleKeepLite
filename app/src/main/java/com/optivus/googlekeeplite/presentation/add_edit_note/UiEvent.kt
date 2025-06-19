package com.optivus.googlekeeplite.presentation.add_edit_note

// In AddEditNoteState.kt or a new file
sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object SaveNote : UiEvent()
    object NavigateBack : UiEvent()
}