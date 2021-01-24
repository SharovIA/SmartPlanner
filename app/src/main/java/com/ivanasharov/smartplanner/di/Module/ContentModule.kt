package com.ivanasharov.smartplanner.di.Module

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.location.LocationManager
import com.ivanasharov.smartplanner.BaseApplication
import com.ivanasharov.smartplanner.clients.interfaces.ServerClient
import com.ivanasharov.smartplanner.clients.interfaces.ServerClientImpl
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.TaskDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ContentModule {

    @Singleton
    @Provides
    fun providesContentResolver(application: Application): ContentResolver {
        return application.contentResolver
    }

    @Singleton
    @Provides
    fun providesLocationManager(application: Application): LocationManager {
        return application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideServerClient(): ServerClient {
        return ServerClientImpl()
    }

}