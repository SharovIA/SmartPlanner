package com.ivanasharov.smartplanner.di.module

import android.content.Context
import androidx.room.Room
import com.ivanasharov.smartplanner.data.database.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule{

    @Singleton
    @Provides
    fun providesTaskDatabase(@ApplicationContext context: Context) : TaskDatabase {
        return Room.databaseBuilder(context, TaskDatabase::class.java,
        "Task.db").build()
    }

    @Singleton
    @Provides
    fun providesTaskDao(taskDatabase: TaskDatabase): TaskDao { return taskDatabase.getTaskDao()}

}