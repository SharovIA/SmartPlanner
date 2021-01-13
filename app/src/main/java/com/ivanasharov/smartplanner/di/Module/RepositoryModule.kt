package com.ivanasharov.smartplanner.di.Module

import android.content.ContentResolver
import com.ivanasharov.smartplanner.data.*
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

    @Singleton
    @Provides
    fun providesContactRepository(contentResolver: ContentResolver): ContactRepository = ContactRepositoryImpl(contentResolver)


}