package com.optivus.googlekeeplite.presentation.add_edit_note

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
    data class LoadNote(val noteId: Int) : AddEditNoteEvent()
    data class ShowSnackbar(val message: String) : AddEditNoteEvent()
    data class ChangeTimestamp(val timestamp: Long) : AddEditNoteEvent()
    object ResetFields : AddEditNoteEvent()
}