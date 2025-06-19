package com.optivus.googlekeeplite.presentation.note_list

sealed class UiEvent {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : UiEvent()
}