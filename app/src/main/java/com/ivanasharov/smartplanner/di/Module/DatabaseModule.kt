package com.ivanasharov.smartplanner.di.Module

import android.content.Context
import androidx.room.Room
import com.ivanasharov.smartplanner.data.dao.TaskDao
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
    fun providesTaskDao(taskDatabase: TaskDatabase): TaskDao { return taskDatabase.getTeskDao()}


//class DatabaseModule(baseApplication: BaseApplication) {

/*    private val taskDatabase : TaskDatabase = Room.databaseBuilder(baseApplication,
        TaskDatabase::class.java,
    "Task.db").build()

    @Singleton
    @Provides
    fun providesTaskDatabase() : TaskDatabase { return taskDatabase}

    @Singleton
    @Provides
    fun providesTaskDao(taskDatabase: TaskDatabase): TaskDao { return taskDatabase.getTeskDao()}

    @Singleton
    @Provides
    fun TaskRepository(taskDao: TaskDao): TaskRepository { return TaskRepositoryImpl(taskDao)}
*/
}