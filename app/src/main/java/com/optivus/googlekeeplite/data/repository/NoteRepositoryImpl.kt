package com.optivus.googlekeeplite.data.repository

import com.optivus.googlekeeplite.data.local.NoteDao
import com.optivus.googlekeeplite.data.local.NoteEntity
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.upsertNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    override suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

    override suspend fun updateNote(note: Note) {
        noteDao.upsertNote(note.toEntity())
    }

    override suspend fun searchNotesByTitle(query: String): Flow<List<Note>> {
        return noteDao.searchNotesByTitle(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNotesCount(): Int {
        return noteDao.getNotesCount()
    }
    // Extension functions to convert between layers can be added here

    // this function converts a NoteEntity to a Note domain model
    private fun NoteEntity.toDomain(): Note {
        return Note(id, title, content, timestamp, color)
    }

    // this function converts a Note domain model to a NoteEntity
    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(id, title, content, timestamp, color)
    }


}