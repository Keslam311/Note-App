package com.example.agenda.di

import android.content.Context
import androidx.room.Room
import com.example.agenda.data.db.AgendaDao
import com.example.agenda.data.db.AgendaDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule{

    @Provides
    @Singleton
   fun provideDataBase(@ApplicationContext context: Context): AgendaDataBase {
       return Room.databaseBuilder(
           context.applicationContext,
           AgendaDataBase::class.java,
           "database"
       ).build()
   }


    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase:AgendaDataBase): AgendaDao {
        return noteDatabase.agendaDao()
    }

}