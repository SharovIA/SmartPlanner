package com.ivanasharov.smartplanner.di.module

import android.content.ContentResolver
import android.content.Context
import android.location.LocationManager
import com.ivanasharov.smartplanner.data.clients.server.ServerClientImpl
import com.ivanasharov.smartplanner.data.database.dao.TaskDao
import com.ivanasharov.smartplanner.data.repositories.weather.RemoteWeatherRepository
import com.ivanasharov.smartplanner.data.repositories.weather.RemoteWeatherRepositoryImpl
import com.ivanasharov.smartplanner.data.repositories.calendar.CalendarRepository
import com.ivanasharov.smartplanner.data.repositories.calendar.CalendarRepositoryImpl
import com.ivanasharov.smartplanner.data.repositories.contacts.ContactRepository
import com.ivanasharov.smartplanner.data.repositories.contacts.ContactRepositoryImpl
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module(includes = arrayOf(DatabaseModule::class))
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providesTaskRepository(taskDao: TaskDao): TaskRepository =
        TaskRepositoryImpl(
            taskDao
        )

    @Singleton
    @Provides
    fun providesCalendarRepository(contentResolver: ContentResolver): CalendarRepository =
        CalendarRepositoryImpl(
            contentResolver
        )

    @Singleton
    @Provides
    fun providesContactRepository(contentResolver: ContentResolver): ContactRepository =
        ContactRepositoryImpl(
            contentResolver
        )

    @Singleton
    @Provides
    fun providesRemoteWeatherRepository(serverClient: ServerClientImpl, locationManager: LocationManager, @ApplicationContext context: Context): RemoteWeatherRepository =
        RemoteWeatherRepositoryImpl(
            serverClient,
            locationManager,
            context
        )



}