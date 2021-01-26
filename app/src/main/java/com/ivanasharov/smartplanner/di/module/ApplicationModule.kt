package com.ivanasharov.smartplanner.di.module

import com.ivanasharov.smartplanner.utils.resources.AndroidResourceProvider
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import dagger.Binds
import dagger.Module
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



