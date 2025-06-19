package com.optivus.googlekeeplite.domain.usecases.usecase

import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}

