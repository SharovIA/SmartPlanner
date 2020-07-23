package com.ivanasharov.smartplanner.di.Module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ivanasharov.smartplanner.BaseApplication
import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.Utils.AndroidResourceProvider
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.data.TaskRepositoryImpl
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.TaskDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DatabaseModule{

    @Singleton
    @Provides
    fun providesTaskDatabase(context: Context) : TaskDatabase {
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