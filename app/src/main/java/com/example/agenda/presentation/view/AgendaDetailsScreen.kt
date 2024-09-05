package com.example.agenda.presentation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.agenda.data.db.Agenda
import com.example.agenda.presentation.viewModel.AgendaViewModel
import com.example.agenda.ui.theme.AgendaTheme

class AgendaDetailsScreen(private val note: Agenda, private val noteViewModel: AgendaViewModel, private val darkTheme:Boolean) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val title = remember { mutableStateOf(note.title) }
        val description = remember { mutableStateOf(note.description) }
        val navigator = LocalNavigator.currentOrThrow

       AgendaTheme(darkTheme = darkTheme) {
           Scaffold (
               topBar = {
                   TopAppBar(
                       title = {
                       },
                       navigationIcon = {
                           IconButton(onClick = {
                               navigator.pop()
                           }) {
                               Icon(
                                   Icons.AutoMirrored.Filled.ArrowBack,
                                   contentDescription = null,
                                   modifier = Modifier.size(50.dp),
                                   tint = if (darkTheme) Color(0xFFFFA61C) else Color.Black
                               )
                           }
                       },
                      colors = TopAppBarColors(
                          containerColor =if (darkTheme) Color.Black else Color(0xffFAFAFA),
                          titleContentColor =if (darkTheme) Color.Black else Color(0xffFAFAFA),
                          actionIconContentColor = if (darkTheme) Color.Black else Color(0xffFAFAFA),
                          scrolledContainerColor = if (darkTheme) Color.Black else Color(0xffFAFAFA),
                          navigationIconContentColor = if (darkTheme) Color.Black else Color(0xffFAFAFA)

                      )
                   )
               }
           ){
               Column(
                   modifier = Modifier
                       .fillMaxSize()
                       .background(if (darkTheme) Color.Black else Color(0xffFAFAFA))
                   ,
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center,
               ) {
                   Spacer(modifier = Modifier.height(10.dp))
                   DismissKeyboardOnClick(
                       text = "Add Title",
                       value = title.value,
                       onValueChange = {
                           title.value = it
                       },
                       height = 100,
                       check = darkTheme
                   )

                   Spacer(modifier = Modifier.height(10.dp))
                   DismissKeyboardOnClick(
                       text = "Add Description",
                       value = description.value,
                       onValueChange = {
                           description.value = it
                       },
                       height = 200,
                       check = darkTheme
                   )
                   Row(modifier = Modifier.padding(horizontal = 40.dp)) {
                       Button(onClick = {
                           noteViewModel.deleteNote(note)
                           navigator.pop()
                       },
                           modifier = Modifier.weight(1f),
                           colors = ButtonDefaults.buttonColors(containerColor =if (darkTheme) Color(0xFFFFA61C) else Color(0xffFAFAFA) )
                       ) {
                           Text(text = "Delete",color=if(darkTheme) Color.White else Color.Black)
                       }
                       Spacer(modifier = Modifier.width(10.dp))
                       Button(onClick = {
                           noteViewModel.updateNote(
                               note.copy(
                                   title = title.value,
                                   description = description.value
                               )
                           )
                           navigator.pop()
                       },
                           modifier = Modifier.weight(1f),
                           colors = ButtonDefaults.buttonColors(containerColor = if (darkTheme) Color(0xFFFFA61C) else Color(0xffFAFAFA) )
                       ) {
                           Text(text = "Update",color=if(darkTheme) Color.White else Color.Black)
                       }



                   }

               }
           }
       }

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissKeyboardOnClick(check:Boolean,height:Int,text: String, value: String, onValueChange: (String) -> Unit) {
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
                .padding(10.dp)
                .height(height.dp)
            ,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedBorderColor = if (check) Color(0xFFF9B751) else Color.Black,
                cursorColor = if (check) Color(0xFFF9B751) else Color.Black,
                focusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedTextColor = if (check) Color(0xFFF9B751) else Color.Black,
                focusedLabelColor = if (check) Color(0xFFF9B751) else Color.Black,
                unfocusedLabelColor =  if (check) Color(0xFFF9B751) else Color.Black,

            ),
            shape = RoundedCornerShape(30.dp),
            label = { Text(text = text) },
        )
    }
}