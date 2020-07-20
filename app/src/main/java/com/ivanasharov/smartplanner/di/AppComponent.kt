package com.ivanasharov.smartplanner.di

import android.content.Context
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.database.TaskDatabase
import com.ivanasharov.smartplanner.di.Module.ApplicationModule
import com.ivanasharov.smartplanner.di.Module.DatabaseModule
import com.ivanasharov.smartplanner.di.Module.DatabaseModule_Factory
import com.ivanasharov.smartplanner.di.Module.TaskRepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, DatabaseModule::class, TaskRepositoryModule::class))
interface AppComponent {

    fun resources(): ResourceProvider
    fun getTaskRepository(): TaskRepository
    fun getTaskDao(): TaskDao
    fun getTaskDatabase(): TaskDatabase

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(context: Context): Builder

        @BindsInstance
        fun databaseModule(databaseModule: DatabaseModule): Builder

        fun build(): AppComponent
    }
}