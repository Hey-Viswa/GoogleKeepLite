package com.optivus.googlekeeplite.domain.usecases.usecase

import com.optivus.googlekeeplite.domain.repository.NoteRepository

class GetNotesCountUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getNotesCount()
    }
}
