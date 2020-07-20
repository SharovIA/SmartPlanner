package com.ivanasharov.smartplanner.di.Module

import android.app.Application
import com.ivanasharov.smartplanner.BaseApplication
import com.ivanasharov.smartplanner.Utils.AndroidResourceProvider
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindResourceProvider(provider: AndroidResourceProvider) : ResourceProvider
}