package com.ivanasharov.smartplanner.di.Module

import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.data.TaskRepositoryImpl
import com.ivanasharov.smartplanner.data.dao.TaskDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class TaskRepositoryModule {

    @Singleton
    @Provides
    fun providesTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepositoryImpl(taskDao)
}