package com.optivus.googlekeeplite.presentation.note_list.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.usecases.model.NoteUseCases
import com.optivus.googlekeeplite.domain.util.NoteOrder
import com.optivus.googlekeeplite.presentation.note_list.NoteListEvent
import com.optivus.googlekeeplite.presentation.note_list.NoteListState
import com.optivus.googlekeeplite.presentation.note_list.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var recentlyDeletedNote: Note? = null

    init {
        loadNotes()
    }


    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnDeleteNote -> {
                viewModelScope.launch {
                    noteUseCase.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                    _uiEvent.emit(UiEvent.ShowSnackbar("Note deleted", "Undo"))
                    loadNotes()
                }
            }

            is NoteListEvent.OnAddNote -> {
                viewModelScope.launch {
                    noteUseCase.addNote(event.note)
                    _uiEvent.emit(UiEvent.ShowSnackbar("Note added", null))
                    loadNotes()
                }
            }

            is NoteListEvent.OnRestoreNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote?.let {
                        noteUseCase.addNote(it)
                        recentlyDeletedNote = null
                        loadNotes()
                    }
                }
            }

            is NoteListEvent.OnToggleSearch -> {
                _state.update { it.copy(isSearchActive = !_state.value.isSearchActive) }
                if (!_state.value.isSearchActive) {
                    _state.update { it.copy(searchQuery = "") }
                    loadNotes()
                }
            }

            is NoteListEvent.OnClearSearch -> {
                _state.update { it.copy(searchQuery = "") }
                searchOrFilterNotes()
            }

            is NoteListEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = event.query) }
                searchOrFilterNotes()
            }

            is NoteListEvent.OnFilterByColor -> {
                _state.update { it.copy(filterByColor = event.colorIndex) }
                searchOrFilterNotes()
            }

            is NoteListEvent.OnToggleSortOrder -> {
                _state.update { it.copy(sortByDateAscending = event.ascending) }
                searchOrFilterNotes()
            }

            is NoteListEvent.OnClearFilters -> {
                _state.update {
                    it.copy(
                        filterByColor = null,
                        sortByDateAscending = false
                    )
                }
                searchOrFilterNotes()
            }
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            noteUseCase.getNotes().collect { notes ->
                _state.update { it.copy(notes = notes, isLoading = false, error = null) }
            }
        }
    }

    private fun searchOrFilterNotes() {
        viewModelScope.launch {
            // Show loading state
            _state.update { it.copy(isLoading = true) }

            try {
                // Get search parameters from state
                val query = _state.value.searchQuery
                val filterByColor = _state.value.filterByColor
                val ascending = _state.value.sortByDateAscending

                // If no filters are applied, just load all notes
                if (query.isBlank() && filterByColor == null && !ascending) {
                    loadNotes()
                    return@launch
                }

                // Get all notes first, then filter them manually
                noteUseCase.getNotes().collect { allNotes ->
                    // 1. Filter by search query (title or content)
                    var filteredNotes = if (query.isBlank()) {
                        allNotes
                    } else {
                        allNotes.filter { note ->
                            (note.title?.contains(query, ignoreCase = true) ?: false) ||
                            (note.content?.contains(query, ignoreCase = true) ?: false)
                        }
                    }

                    // 2. Filter by color if needed
                    if (filterByColor != null) {
                        filteredNotes = filteredNotes.filter { it.color == filterByColor }
                    }

                    // 3. Sort filtered notes based on NoteOrder
                    filteredNotes = when (_state.value.noteOrder) {
                        is NoteOrder.DateAsc -> filteredNotes.sortedBy { it.timestamp }
                        is NoteOrder.DateDesc -> filteredNotes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> filteredNotes.sortedBy { it.color }
                        else -> {
                            // Fallback to the boolean flag for backward compatibility
                            if (ascending) {
                                filteredNotes.sortedBy { it.timestamp }
                            } else {
                                filteredNotes.sortedByDescending { it.timestamp }
                            }
                        }
                    }

                    // Update UI state with filtered and sorted notes
                    _state.update {
                        it.copy(
                            notes = filteredNotes,
                            isLoading = false,
                            error = if (filteredNotes.isEmpty() && query.isNotBlank())
                                "No notes found for '$query'"
                            else
                                null
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle any errors and show error message
                _state.update {
                    it.copy(
                        error = "Search error: ${e.localizedMessage ?: "Unknown error"}",
                        isLoading = false
                    )
                }
                e.printStackTrace()
            }
        }
    }
}