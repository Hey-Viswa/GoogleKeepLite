package com.optivus.googlekeeplite.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.optivus.googlekeeplite.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Inserts or updates a note in the database
    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    // Deletes a specific note from the database
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // Returns a flow of all notes ordered by timestamp descending
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // Returns a note by its ID, or null if not found
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity?

    // Deletes all notes from the database
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    // Searches for notes with titles matching the query
    @Query("SELECT * FROM notes WHERE title LIKE :query")
    fun searchNotesByTitle(query: String): Flow<List<NoteEntity>>

    // Returns the total count of notes in the database
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Int
}