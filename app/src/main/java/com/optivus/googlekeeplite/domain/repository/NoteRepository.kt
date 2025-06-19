package com.optivus.googlekeeplite.domain.repository

import com.optivus.googlekeeplite.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    // Define the methods that the repository will implement
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteAllNotes()
    suspend fun updateNote(note: Note)
    suspend fun searchNotesByTitle(query: String): Flow<List<Note>>
    suspend fun getNotesCount(): Int
}