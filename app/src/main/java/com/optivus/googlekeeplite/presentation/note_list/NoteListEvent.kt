package com.optivus.googlekeeplite.presentation.note_list

import com.optivus.googlekeeplite.domain.model.Note


sealed class NoteListEvent {
    data class OnSearchQueryChange(val query: String) : NoteListEvent()
    data class OnDeleteNote(val note: Note) : NoteListEvent()
    data class OnAddNote(val note: Note) : NoteListEvent()
    data class OnFilterByColor(val colorIndex: Int?) : NoteListEvent()
    data class OnToggleSortOrder(val ascending: Boolean) : NoteListEvent()
    object OnRestoreNote : NoteListEvent()
    object OnToggleSearch : NoteListEvent()
    object OnClearSearch : NoteListEvent()
    object OnClearFilters : NoteListEvent()
}
