package com.optivus.googlekeeplite.presentation.add_edit_note.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.domain.usecases.model.NoteUseCases
import com.optivus.googlekeeplite.presentation.add_edit_note.AddEditNoteEvent
import com.optivus.googlekeeplite.presentation.add_edit_note.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class AddEditNoteViewModelTest {

    // Test coroutine setup
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    @Mock
    private lateinit var noteUseCases: NoteUseCases

    // System under test
    private lateinit var viewModel: AddEditNoteViewModel

    @Before
    suspend fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Setup mock behavior to avoid NPE
        doNothing().`when`(noteUseCases).addNote(any())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when title is entered, state is updated`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)

        // When
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("New Title"))
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assert(viewModel.state.value.title == "New Title")
    }

    @Test
    fun `when content is entered, state is updated`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)

        // When
        viewModel.onEvent(AddEditNoteEvent.EnteredContent("New Content"))
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assert(viewModel.state.value.content == "New Content")
    }

    @Test
    fun `when color is changed, state is updated`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)
        val newColor = 0xFFABCDEF.toInt()

        // When
        viewModel.onEvent(AddEditNoteEvent.ChangeColor(newColor))
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assert(viewModel.state.value.color == newColor)
    }

    @Test
    fun `when fields are reset, state returns to default`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)

        // Set some values first
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("Some Title"))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent("Some Content"))
        advanceUntilIdle() // Ensure all coroutines complete

        // When
        viewModel.onEvent(AddEditNoteEvent.ResetFields)
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assert(viewModel.state.value.title == "")
        assert(viewModel.state.value.content == "")
    }

    @Test
    fun `when saving empty note, shows error snackbar`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)

        // Collect UI events
        val uiEvents = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toList(uiEvents)
        }

        // When
        viewModel.onEvent(AddEditNoteEvent.SaveNote)
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        assert(uiEvents.isNotEmpty()) { "Expected UI events but was empty" }
        assert(uiEvents[0] is UiEvent.ShowSnackbar) { "Expected ShowSnackbar but was ${uiEvents[0]::class.simpleName}" }
        assert((uiEvents[0] as UiEvent.ShowSnackbar).message.contains("empty note")) {
            "Expected message to contain 'empty note' but was '${(uiEvents[0] as UiEvent.ShowSnackbar).message}'"
        }

        job.cancel()
    }

    @Test
    fun `when saving valid note, note is added`() = runTest {
        // Given
        val savedStateHandle = SavedStateHandle()
        viewModel = AddEditNoteViewModel(noteUseCases, savedStateHandle)

        // Set valid values
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("Valid Title"))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent("Valid Content"))
        advanceUntilIdle() // Ensure all coroutines complete

        // Collect UI events
        val uiEvents = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toList(uiEvents)
        }

        // When
        viewModel.onEvent(AddEditNoteEvent.SaveNote)
        advanceUntilIdle() // Ensure all coroutines complete

        // Then
        verify(noteUseCases).addNote(any())

        assert(uiEvents.isNotEmpty()) { "Expected UI events but was empty" }
        assert(uiEvents[0] is UiEvent.SaveNote) { "Expected SaveNote but was ${uiEvents[0]::class.simpleName}" }

        job.cancel()
    }
}
