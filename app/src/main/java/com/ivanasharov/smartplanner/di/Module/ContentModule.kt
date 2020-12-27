package com.ivanasharov.smartplanner.di.Module

import android.app.Application
import android.content.ContentResolver
import com.ivanasharov.smartplanner.BaseApplication
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
    fun providesTaskDao(application: Application): ContentResolver {
        return application.contentResolver
    }

}