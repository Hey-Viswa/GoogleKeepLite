package com.optivus.googlekeeplite.domain.usecases.usecase

import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(
        query: String,
        filterByColor: Int? = null,
        sortByDate: Boolean = true,
        ascending: Boolean = false
    ): Flow<List<Note>> {
        return repository.getAllNotes().map { notes ->
            var filteredNotes = notes.filter { note ->
                val matchesQuery = note.title?.contains(query, ignoreCase = true) == true ||
                        note.content?.contains(query, ignoreCase = true) == true

                val matchesColor = if (filterByColor != null) {
                    note.color == filterByColor
                } else {
                    true
                }

                matchesQuery && matchesColor
            }

            // Apply sorting
            if (sortByDate) {
                filteredNotes = if (ascending) {
                    filteredNotes.sortedBy { it.timestamp }
                } else {
                    filteredNotes.sortedByDescending { it.timestamp }
                }
            }

            filteredNotes
        }
    }
}
