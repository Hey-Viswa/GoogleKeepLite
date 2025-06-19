package com.optivus.googlekeeplite.domain.usecases.model

import com.optivus.googlekeeplite.domain.usecases.usecase.AddNoteUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.DeleteNoteUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNoteByIdUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNotesCountUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNotesUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.SearchNotesUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.UpdateNoteUseCase

data class NoteUseCases(
    val addNote: AddNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNotes: GetNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val updateNote: UpdateNoteUseCase,
    val searchNotes: SearchNotesUseCase,
    val getNotesCount: GetNotesCountUseCase
)