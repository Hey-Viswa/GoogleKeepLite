package com.optivus.googlekeeplite.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.presentation.utils.ColorUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A compact version of NoteItem used for displaying search results
 */
@Composable
fun NoteItemCompact(
    note: Note,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine if we're in dark mode
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Get the correct color based on the note's color index and dark mode
    val noteColor = ColorUtils.getNoteColor(note.color, isDarkMode)

    // Determine text color based on background brightness
    val textColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.87f)
    } else {
        Color.Black.copy(alpha = 0.87f)
    }

    // Format the date
    val formattedDate = SimpleDateFormat("MMM dd", Locale.getDefault())
        .format(Date(note.timestamp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNoteClick(note) },
        colors = CardDefaults.cardColors(
            containerColor = noteColor,
            contentColor = textColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (!note.title.isNullOrBlank()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (!note.content.isNullOrBlank()) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Date indicator
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.6f)
            )
        }
    }
}
