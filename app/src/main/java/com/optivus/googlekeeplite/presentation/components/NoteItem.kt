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
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine if we're in dark mode
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Get the correct color based on the note's color index and dark mode
    val noteColor = ColorUtils
        .getNoteColor(note.color, isDarkMode)

    // Determine text color based on background brightness
    val textColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.87f)
    } else {
        Color.Black.copy(alpha = 0.87f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onNoteClick(note) }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = noteColor,
            contentColor = textColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
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
                IconButton(onClick = { onDeleteClick(note) }) {
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