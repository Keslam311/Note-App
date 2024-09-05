package com.example.agenda.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface AgendaDao {

    @Query("SELECT * FROM agenda")
    suspend fun getAll(): List<Agenda>

    @Insert
    suspend fun insert(note: Agenda)

    @Update
    suspend fun update(note: Agenda)

    @Delete
    suspend fun deleteAgenda(note: Agenda)

}
