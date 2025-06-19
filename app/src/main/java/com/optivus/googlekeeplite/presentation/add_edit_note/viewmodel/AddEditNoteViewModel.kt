package com.optivus.googlekeeplite.presentation.add_edit_note.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.usecases.model.NoteUseCases
import com.optivus.googlekeeplite.presentation.add_edit_note.AddEditNoteEvent
import com.optivus.googlekeeplite.presentation.add_edit_note.AddEditNoteState
import com.optivus.googlekeeplite.presentation.add_edit_note.UiEvent
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
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditNoteState(timestamp = System.currentTimeMillis()))
    val state: StateFlow<AddEditNoteState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        val noteId = savedStateHandle.get<Int>("noteId") ?: -1
        if (noteId != -1) {
            loadNote(noteId)
        }
    }

    private fun loadNote(noteId: Int) {
        viewModelScope.launch {
            try {
                noteUseCase.getNoteById(noteId)?.let { note ->
                    currentNoteId = note.id
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            color = note.color,
                            timestamp = note.timestamp
                        )
                    }
                } ?: run {
                    _uiEvent.emit(UiEvent.ShowSnackbar("Note not found"))
                }
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Error loading note: ${e.message}"))
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _state.update {
                    it.copy(title = event.value)
                }
            }

            is AddEditNoteEvent.EnteredContent -> {
                _state.update {
                    it.copy(content = event.value)
                }
            }

            is AddEditNoteEvent.ChangeColor -> {
                _state.update {
                    it.copy(color = event.color)
                }
            }

            is AddEditNoteEvent.ChangeTimestamp -> {
                _state.update {
                    it.copy(timestamp = event.timestamp)
                }
            }

            is AddEditNoteEvent.LoadNote -> {
                loadNote(event.noteId)
            }

            is AddEditNoteEvent.ResetFields -> {
                currentNoteId = null
                _state.value = AddEditNoteState(timestamp = System.currentTimeMillis())
            }

            is AddEditNoteEvent.ShowSnackbar -> {
                viewModelScope.launch {
                    _uiEvent.emit(UiEvent.ShowSnackbar(event.message))
                }
            }

            is AddEditNoteEvent.SaveNote -> {
                saveNote()
            }
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                if (_state.value.title.isBlank() && _state.value.content.isBlank()) {
                    _uiEvent.emit(UiEvent.ShowSnackbar("Cannot save empty note"))
                    return@launch
                }
                val note = Note(
                    id = currentNoteId,
                    title = _state.value.title,
                    content = _state.value.content,
                    color = _state.value.color,
                    timestamp = _state.value.timestamp
                )
                noteUseCase.addNote(note)
                _uiEvent.emit(UiEvent.SaveNote)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Error saving note: ${e.message}"))
            }
        }
    }
}