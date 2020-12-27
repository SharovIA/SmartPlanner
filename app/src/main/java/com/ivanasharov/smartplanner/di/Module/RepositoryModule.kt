package com.ivanasharov.smartplanner.di.Module

import android.content.ContentResolver
import com.ivanasharov.smartplanner.data.CalendarRepository
import com.ivanasharov.smartplanner.data.CalendarRepositoryImpl
import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.data.TaskRepositoryImpl
import com.ivanasharov.smartplanner.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module(includes = arrayOf(DatabaseModule::class))
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providesTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepositoryImpl(taskDao)

    @Singleton
    @Provides
    fun providesCalendarRepository(contentResolver: ContentResolver): CalendarRepository = CalendarRepositoryImpl(contentResolver)


}