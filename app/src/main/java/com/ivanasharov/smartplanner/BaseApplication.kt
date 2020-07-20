package com.ivanasharov.smartplanner

import android.app.Application
import com.ivanasharov.smartplanner.data.database.TaskDatabase
import com.ivanasharov.smartplanner.di.AppComponent
import com.ivanasharov.smartplanner.di.DaggerAppComponent

import com.ivanasharov.smartplanner.di.Module.ApplicationModule
import com.ivanasharov.smartplanner.di.Module.DatabaseModule
import javax.inject.Inject

class BaseApplication : Application() {


/*   companion object{
        private lateinit var component: AppComponent
        private lateinit var baseApplication : BaseApplication

        fun getComponent() : AppComponent = component
        fun getInstance() : BaseApplication = baseApplication
    }
*/
    override fun onCreate() {
        super.onCreate()
   //     baseApplication = this

        initializeComponent()

    }

    private fun initializeComponent() {
      /*  component =
            DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(baseApplication))
            .databaseModule(DatabaseModule(baseApplication))
            .build()
       */
        DI.appComponent = DaggerAppComponent.builder()
            .appContext(this)
          // .appComponent()
            .taskReposiitiryModule()
            .databaseModule(DatabaseModule(applicationContext))
            .build()
    }
}