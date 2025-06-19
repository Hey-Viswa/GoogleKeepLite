package com.optivus.googlekeeplite.domain.usecases.usecase

import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
