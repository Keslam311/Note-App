package com.example.agenda.presentation.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.agenda.AddNoteDialog
import com.example.agenda.data.db.Agenda
import com.example.agenda.presentation.viewModel.AgendaViewModel
import com.example.agenda.ui.theme.AgendaTheme
import com.example.noteapp.HomeScreenContent


class MainScreen(private val noteViewModel: AgendaViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val showAddDialog = remember { mutableStateOf(false) }
        val isSelected = remember { mutableStateOf(true) }
        var value by remember { mutableStateOf("") }
        var darkTheme by rememberSaveable { mutableStateOf(true) }
        val sharedPreferences: SharedPreferences =
            LocalContext.current.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean("darkTheme", true)
        AgendaTheme(darkTheme = darkTheme) {
            Scaffold(
                floatingActionButton = {
                    IconButton(modifier = Modifier.size(80.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (darkTheme) Color(0xff1C1B20) else Color.White,
                            contentColor = if (darkTheme) Color.White else Color.Black
                        ),
                        onClick = {
                            showAddDialog.value = true
                        }) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        actions = {
                            // Action buttons
                            IconButton(onClick = { }) {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = if (darkTheme) Color.White else Color.Black
                                )
                            }

                        },
                        navigationIcon = {
                            Switch(checked = darkTheme, onCheckedChange = {
                                darkTheme = it
                                sharedPreferences.edit().putBoolean("darkTheme", it).apply()
                            },
                                colors = SwitchDefaults.colors(
                                    if (darkTheme) Color.Black else Color.Black,
                                    checkedIconColor = if (darkTheme) Color.White else Color.Black,
                                    checkedBorderColor = if (darkTheme) Color.White else Color.Black,
                                    uncheckedThumbColor = if (darkTheme) Color.White else Color.Black,
                                    uncheckedTrackColor = Color.White,
                                    uncheckedBorderColor = if (darkTheme) Color.White else Color.Black,
                                    checkedTrackColor = if (darkTheme) Color.White else Color.White,
                                )

                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            titleContentColor = Color.White,
                            containerColor = if (darkTheme) Color(0xff1C1B20) else Color.White
                        ),
                        title = {
                            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                                Text(
                                    text = "MyNotes",
                                    fontSize = 30.sp,
                                    color = if (darkTheme) Color.White else Color.Black
                                )
                            }
                        },


                        )
                }, modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    DismissKeyboardOnClickOutside(
                        text = "Search",
                        value = value,
                        onValueChange = {
                            value = it
                        },
                        check = darkTheme
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = { isSelected.value = true }, modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "All Notes",
                                color = if (isSelected.value && !darkTheme) {
                                    Color.Black
                                } else if (isSelected.value && darkTheme) {
                                    Color.White
                                } else if (!isSelected.value && !darkTheme) {
                                    Color.Gray
                                } else {
                                    Color.Gray
                                }
                            )

                        }
                        TextButton(
                            onClick = { isSelected.value = false }, modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Favorite Notes",
                                color = if (isSelected.value && !darkTheme) {
                                    Color.Gray
                                } else if (isSelected.value && darkTheme) {
                                    Color.Gray
                                } else if (!isSelected.value && !darkTheme) {
                                    Color.Black
                                } else {
                                    Color.White
                                }
                            )
                        }
                    }
                    // screen content
                    if (showAddDialog.value) {
                        AddNoteDialog(
                            onSaveClick = { title, description, color ->
                                noteViewModel.insertNote(
                                    Agenda(
                                        title = title,
                                        description = description,
                                        isFavorite = false,
                                        color = color
                                    )
                                )
                                showAddDialog.value = false
                            },
                            onDismissRequest = {
                                showAddDialog.value = false
                            },
                            check = darkTheme
                        )

                    }
                    if (isSelected.value) {
                        HomeScreenContent(noteViewModel = noteViewModel, darkTheme)
                    } else {
                        FavoriteScreenContent(noteViewModel = noteViewModel, darkTheme)
                    }
                    // HomeScreenContent(noteViewModel = noteViewModel)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissKeyboardOnClickOutside(
    check: Boolean,
    text: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // Obtain the FocusManager to control focus behavior
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // Clear focus when tapping outside
                    focusManager.clearFocus()
                })
            }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (check) Color.White else Color.Black,
                unfocusedBorderColor = if (check) Color.White else Color.Black,
                cursorColor = if (check) Color.White else Color.Black,
                focusedTextColor = if (check) Color.White else Color.Black,
                unfocusedTextColor = if (check) Color.White else Color.Black,
                focusedPlaceholderColor = if (check) Color.White else Color.Black,
                unfocusedPlaceholderColor = if (check) Color.White else Color.Black,
            ),
            shape = RoundedCornerShape(30.dp),
            placeholder = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }
}