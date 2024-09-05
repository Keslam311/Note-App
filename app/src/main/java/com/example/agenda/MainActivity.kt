package com.example.agenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.agenda.presentation.view.MainScreen
import com.example.agenda.presentation.viewModel.AgendaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val noteViewModel: AgendaViewModel = hiltViewModel()
            Navigator(screen = MainScreen(noteViewModel))
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(check:Boolean,onSaveClick: (String, String, Long) -> Unit, onDismissRequest: () -> Unit) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val selectedColor = remember { mutableLongStateOf(0xFFffab91) }
    AlertDialog(onDismissRequest = { onDismissRequest() },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = if (check) Color(0xFFFFA61C) else Color(0xffDCD4EB))
            ) {
                Text(text = "Cancel")
            }
        },
        containerColor = if (check) Color.Black else Color(0xffDCD4EB),
        confirmButton = {
            Button(onClick = {
                onSaveClick(
                    title.value,
                    description.value,
                    selectedColor.longValue
                )
            }, colors = ButtonDefaults.buttonColors(containerColor = if (check) Color(0xFFFFA61C) else Color(0xffDCD4EB))) {
                Text(text = "Save Note")
            }
        },
        title = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Add New Note", color = if (check) Color(0xFFF9B751) else Color.Black)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        cursorColor =if (check) Color(0xFFF9B751) else Color.Black,
                        focusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                        focusedPlaceholderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedPlaceholderColor = if (check) Color(0xFFF9B751) else Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(30.dp),
                    placeholder = { Text(text = "Title") },
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(110.dp)
                    ,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        cursorColor = if (check) Color(0xFFF9B751) else Color.Black,
                        focusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                        focusedPlaceholderColor = if (check) Color(0xFFF9B751) else Color.Black,
                        unfocusedPlaceholderColor = if (check) Color(0xFFF9B751) else Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(30.dp),
                    placeholder = { Text(text = "Description") },
                )
                selectedColor.longValue = dropDownMenu(check)
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropDownMenu(check:Boolean): Long {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Red", "Orange", "Green")
//    val colors = listOf(Color(0xFFffab91), Color(0xFFffcc80), Color(0xFFe6ee9b), Color(0xFF80deea))
    var selectedColor by remember { mutableStateOf(0xFFffab91) }
    var value by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            readOnly = true,
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            placeholder = { Text("Color") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded }, tint =  if (check) Color(0xFFF9B751) else Color.Black
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor =  if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedBorderColor =  if (check) Color(0xFFF9B751) else Color.Black,
                cursorColor =  if (check) Color(0xFFF9B751) else Color.Black,
                focusedTextColor =  if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedTextColor =  if (check) Color(0xFFF9B751) else Color.Black,
                focusedPlaceholderColor =  if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedPlaceholderColor =  if (check) Color(0xFFF9B751) else Color.Black,
            ),
            shape = RoundedCornerShape(30.dp),

            )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current)
                { textFieldSize.width.toDp() })
                .background( if (check) Color(0xFFF9B751) else Color(0xffDCD4EB))
        ) {
            suggestions.forEachIndexed { index, label ->
                print(index)
                DropdownMenuItem(
                    text = { Text(text = label) }, onClick = {
                        value = label
                        expanded = false
                    })
            }
        }
    }
    if (value == "Red") {
        selectedColor = 0xFFffab91
    } else if (value == "Orange") {
        selectedColor = 0xFFffcc80
    } else  {
        selectedColor = 0xFFe6ee9b
    }
    return selectedColor
}




