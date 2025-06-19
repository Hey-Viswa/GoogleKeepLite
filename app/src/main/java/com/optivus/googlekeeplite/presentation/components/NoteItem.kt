package com.optivus.googlekeeplite.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.presentation.utils.ColorUtils



@Composable
fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    noteStyle: com.optivus.googlekeeplite.presentation.settings.NoteStyle = com.optivus.googlekeeplite.presentation.settings.NoteStyle.DEFAULT
) {
    // Determine if we're in dark mode
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Get the note color using ColorUtils to ensure consistency
    val noteColor = ColorUtils.getNoteColor(note.color, isDarkMode)

    // Determine appropriate text color based on background brightness
    val textColor = if (noteColor.luminance() > 0.5f) {
        Color.Black.copy(alpha = 0.87f)
    } else {
        Color.White.copy(alpha = 0.87f)
    }

    // Apply different card styles based on noteStyle setting
    val cardShape = when (noteStyle) {
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.ROUNDED -> MaterialTheme.shapes.extraLarge
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MATERIAL -> MaterialTheme.shapes.large
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MINIMAL -> MaterialTheme.shapes.small
        else -> MaterialTheme.shapes.medium
    }

    val cardElevation = when (noteStyle) {
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MATERIAL -> CardDefaults.cardElevation(defaultElevation = 4.dp)
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.ROUNDED -> CardDefaults.cardElevation(defaultElevation = 2.dp)
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MINIMAL -> CardDefaults.cardElevation(defaultElevation = 0.dp)
        else -> CardDefaults.cardElevation(defaultElevation = 1.dp)
    }

    val cardPadding = when (noteStyle) {
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MINIMAL -> 8.dp
        com.optivus.googlekeeplite.presentation.settings.NoteStyle.MATERIAL -> 16.dp
        else -> 12.dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(cardShape)
            .clickable(onClick = onNoteClick),
        colors = CardDefaults.cardColors(
            containerColor = noteColor
        ),
        elevation = cardElevation,
        shape = cardShape
    ) {
        Column(
            modifier = Modifier.padding(cardPadding)
        ) {
            if (note.title.isNotBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Note",
                        tint = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun NoteItemPreview() {
    NoteItem(
        note = Note(
            id = 1,
            title = "Sample Note",
            content = "This is a sample note content for preview.",
            timestamp = System.currentTimeMillis(),
            color = 0
        ),
        onNoteClick = {},
        onDeleteClick = {}
    )
}