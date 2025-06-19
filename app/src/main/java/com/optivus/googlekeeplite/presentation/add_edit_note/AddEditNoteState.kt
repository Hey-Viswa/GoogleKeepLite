package com.optivus.googlekeeplite.presentation.add_edit_note

import com.optivus.googlekeeplite.domain.model.Note

data class AddEditNoteState(
    val title: String = "",
    val content: String = "",
    val color: Int = Note.DEFAULT_COLOR,
    val timestamp: Long = System.currentTimeMillis(),
    val isNoteSaved: Boolean = false,
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val isSaving: Boolean = false
)