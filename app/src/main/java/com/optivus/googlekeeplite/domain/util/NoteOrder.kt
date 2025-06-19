package com.optivus.googlekeeplite.domain.util

sealed class NoteOrder {
    object DateDesc : NoteOrder()
    object DateAsc : NoteOrder()
    object Color : NoteOrder()
    // Add more orders as needed
}