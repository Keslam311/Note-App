package com.example.agenda.presentation.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.data.db.Agenda
import com.example.noteapp2.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AgendaViewModel@Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private val _allNotes = MutableStateFlow<List<Agenda>?>(null)
    val allNotes : StateFlow<List<Agenda>?> get() = _allNotes


    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            _allNotes.value = noteRepository.getAll()
            println("All notes: ${_allNotes.value}")
        }
    }
    fun insertNote(note:Agenda){
        viewModelScope.launch {
            noteRepository.insert(note)
            getAllNotes()
        }
    }

    fun updateNote(note:Agenda){
        viewModelScope.launch {
            noteRepository.update(note)
            getAllNotes()

        }
    }

    fun deleteNote(note:Agenda){
        viewModelScope.launch {
            noteRepository.deleteAgenda(note)
            getAllNotes()
        }
    }


    }


