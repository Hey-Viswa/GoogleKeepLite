package com.optivus.googlekeeplite.di

import android.app.Application
import androidx.room.Room
import com.optivus.googlekeeplite.data.local.NoteDao
import com.optivus.googlekeeplite.data.local.NoteDatabase
import com.optivus.googlekeeplite.data.repository.NoteRepositoryImpl
import com.optivus.googlekeeplite.data.repository.SettingsRepositoryImpl
import com.optivus.googlekeeplite.domain.repository.NoteRepository
import com.optivus.googlekeeplite.domain.usecases.model.NoteUseCases
import com.optivus.googlekeeplite.domain.usecases.usecase.AddNoteUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.DeleteNoteUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNoteByIdUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNotesCountUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.GetNotesUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.SearchNotesUseCase
import com.optivus.googlekeeplite.domain.usecases.usecase.UpdateNoteUseCase
import com.optivus.googlekeeplite.presentation.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app, NoteDatabase::class.java, "note_db"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase): NoteDao = db.noteDao

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            addNote = AddNoteUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            getNotes = GetNotesUseCase(repository),
            getNoteById = GetNoteByIdUseCase(repository),
            updateNote = UpdateNoteUseCase(repository),
            searchNotes = SearchNotesUseCase(repository),
            getNotesCount = GetNotesCountUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(app: Application): SettingsRepository {
        return SettingsRepositoryImpl(app)
    }
}