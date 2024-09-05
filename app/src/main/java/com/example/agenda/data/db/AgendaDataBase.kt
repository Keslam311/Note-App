package com.example.agenda.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Agenda::class], version = 4, exportSchema = false)
abstract class AgendaDataBase: RoomDatabase()  {


    abstract fun agendaDao(): AgendaDao

}