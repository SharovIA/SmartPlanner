package com.ivanasharov.smartplanner.di.Module

import android.app.Application
import android.content.Context
import com.ivanasharov.smartplanner.BaseApplication
import com.ivanasharov.smartplanner.Utils.AndroidResourceProvider
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindResourceProvider(provider: AndroidResourceProvider) : ResourceProvider
}



