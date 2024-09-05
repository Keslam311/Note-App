package com.example.noteapp2.data.repository

import android.provider.ContactsContract
import com.example.agenda.data.db.Agenda
import com.example.agenda.data.db.AgendaDao
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class NoteRepository @Inject constructor (private val noteDao: AgendaDao) {
    suspend fun getAll() = noteDao.getAll()
    suspend fun insert(note: Agenda) = noteDao.insert(note)
    suspend fun update(note: Agenda) = noteDao.update(note)
    suspend fun deleteAgenda(note: Agenda) = noteDao.deleteAgenda(note)
}