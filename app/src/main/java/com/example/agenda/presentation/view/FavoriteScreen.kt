package com.example.agenda.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.agenda.util.SwipeToDeleteContainer
import com.example.agenda.presentation.viewModel.AgendaViewModel

@Composable
fun FavoriteScreenContent(noteViewModel: AgendaViewModel, darkThem: Boolean) {
    val allNotes = noteViewModel.allNotes.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(allNotes.value?.filter { it.isFavorite } ?: emptyList()) { note ->
            key(note.id) {
                SwipeToDeleteContainer(item = note, onDelete = {
                    noteViewModel.deleteNote(note)
                }) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = Color(note.color)),
                        elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.push(
                                    AgendaDetailsScreen(
                                        note,
                                        noteViewModel,
                                        darkThem
                                    )
                                )
                            }
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                modifier = Modifier
                                    .clickable {
                                        noteViewModel.updateNote(note.copy(isFavorite = !note.isFavorite))
                                    }
                                    .align(Alignment.End)
                                    .size(30.dp),
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (note.isFavorite) Color.Yellow else Color.White)
                            Text(
                                color = Color.Black,
                                text = note.title,
                                fontSize = 20.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = note.description,
                                fontSize = 20.sp,
                                color = Color.Black,
                                )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}