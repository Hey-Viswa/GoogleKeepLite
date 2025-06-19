package com.optivus.googlekeeplite.domain.usecases.usecase

import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.repository.NoteRepository

class UpdateNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}
