package com.ivanasharov.smartplanner.di.module

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.location.LocationManager
import com.ivanasharov.smartplanner.data.clients.interfaces.ServerClient
import com.ivanasharov.smartplanner.data.clients.server.ServerClientImpl
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
    fun providesServerClient(): ServerClient {
        return ServerClientImpl()
    }

}