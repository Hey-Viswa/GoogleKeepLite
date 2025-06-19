package com.optivus.googlekeeplite.domain.model

data class Note(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val timestamp: Long,
    val color: Int,
) {
    companion object {
        val DEFAULT_COLOR = 0xFFFFAB91.toInt() // Light Red, matching the first color from noteColors
    }
}
