package com.optivus.googlekeeplite.presentation.note_list

import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.util.NoteOrder

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
    val filterByColor: Int? = null,
    val sortByDateAscending: Boolean = false,
    val noteOrder: NoteOrder = NoteOrder.DateDesc
)