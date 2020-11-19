package com.ivanasharov.smartplanner.di.Module

import com.ivanasharov.smartplanner.Utils.AndroidResourceProvider
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.domain.AddTaskInteractorImpl
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelsModule {

    @Binds
    abstract fun addTaskInteractor(addTaskInteractor: AddTaskInteractorImpl): AddTaskInteractor

    @Binds
    abstract fun currentDayInteractor(currentTaskInteractor : CurrentTasksInteractorImpl): CurrentTasksInteractor

}