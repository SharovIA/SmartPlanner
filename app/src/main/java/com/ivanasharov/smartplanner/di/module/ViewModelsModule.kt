package com.ivanasharov.smartplanner.di.module

import com.ivanasharov.smartplanner.domain.interactors.*
import com.ivanasharov.smartplanner.domain.interactors.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelsModule {

    @Binds
    abstract fun bindAddTaskInteractor(addTaskInteractor: AddTaskInteractorImpl): AddTaskInteractor

    @Binds
    abstract fun bindCurrentDayInteractor(currentTaskInteractor : CurrentTasksInteractorImpl): CurrentTasksInteractor

    @Binds
    abstract fun bindDailyScheduleInteractor(dailyScheduleInteractor: DailyScheduleInteractorImpl): DailyScheduleInteractor

    @Binds
    abstract fun bindWeatherInteractor(weatherInteractorImpl: WeatherInteractorImpl): WeatherInteractor

    @Binds
    abstract fun bindPlanningInteractor(planningInteractorImpl: PlanningInteractorImpl): PlanningInteractor

}