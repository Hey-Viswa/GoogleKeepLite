package com.optivus.googlekeeplite.presentation.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.optivus.googlekeeplite.domain.model.Note
import com.optivus.googlekeeplite.presentation.colorpicker.ColorPickerSection

@Composable
fun NoteDialog(
    show: Boolean, note: Note?, onSave: (Note) -> Unit, onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var colorIndex by remember { mutableStateOf(note?.color ?: 0) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showEmptyNoteAlert by remember { mutableStateOf(false) }

    // Determine if we're in dark mode
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Get the note color using ColorUtils to ensure consistency
    val selectedColor = com.optivus.googlekeeplite.presentation.utils.ColorUtils
        .getNoteColor(colorIndex, isDarkMode)

    // Determine appropriate text color based on background brightness
    val textColor = if (selectedColor.luminance() > 0.5f) {
        Color.Black.copy(alpha = 0.87f)
    } else {
        Color.White.copy(alpha = 0.87f)
    }

    // Alert dialog for empty notes
    if (showEmptyNoteAlert) {
        AlertDialog(
            onDismissRequest = { showEmptyNoteAlert = false },
            title = {
                Text(
                    "Empty Note",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    "Note cannot be empty. Please enter a title or content.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showEmptyNoteAlert = false }) {
                    Text(
                        "UNDERSTAND",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(animationSpec = tween(durationMillis = 150)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
        exit = fadeOut(animationSpec = tween(durationMillis = 100)) +
               slideOutVertically(
                   targetOffsetY = { it / 2 },
                   animationSpec = tween(durationMillis = 150)
               )
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = selectedColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Dialog Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                selectedColor.copy(alpha = 0.8f),
                                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (note == null) "Create Note" else "Edit Note",
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    // Note content area
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        TransparentHintTextField(
                            text = title,
                            hint = "Title",
                            onValueChange = { title = it },
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                color = textColor
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TransparentHintTextField(
                            text = content,
                            hint = "Note content",
                            onValueChange = { content = it },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = textColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }

                    // Color picker section (when visible)
                    AnimatedVisibility(
                        visible = showColorPicker,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            ColorPickerSection(
                                selectedColorIndex = colorIndex,
                                onColorSelected = {
                                    colorIndex = it
                                    showColorPicker = false
                                }
                            )
                        }
                    }

                    // Bottom buttons row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                selectedColor.copy(alpha = 0.9f),
                                RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Color palette button
                        IconButton(
                            onClick = { showColorPicker = !showColorPicker },
                            modifier = Modifier
                                .shadow(2.dp, CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                                    CircleShape
                                )
                                .size(42.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Change color",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Cancel button
                            OutlinedButton(
                                onClick = onDismiss,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = textColor
                                ),
                                border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Cancel")
                            }

                            // Save button
                            Button(
                                onClick = {
                                    if (title.isNotBlank() || content.isNotBlank()) {
                                        val updatedNote = Note(
                                            id = note?.id,
                                            title = title,
                                            content = content,
                                            timestamp = System.currentTimeMillis(),
                                            color = colorIndex
                                        )
                                        onSave(updatedNote)
                                    } else {
                                        showEmptyNoteAlert = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NoteDialogPrev() {
    NoteDialog(
        show = true,
        note = Note(
            id = 1,
            title = "Sample Note",
            content = "This is a preview of the note dialog.",
            timestamp = System.currentTimeMillis(),
            color = 5
        ),
        onSave = {},
        onDismiss = {}
    )
}