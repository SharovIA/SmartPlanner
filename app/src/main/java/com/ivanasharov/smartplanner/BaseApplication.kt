package com.ivanasharov.smartplanner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

 /*   companion object{
        private lateinit var baseApplication : BaseApplication

        fun getInstance() : BaseApplication = baseApplication
    }

   companion object{
        private lateinit var component: AppComponent
        private lateinit var baseApplication : BaseApplication

        fun getComponent() : AppComponent = component
        fun getInstance() : BaseApplication = baseApplication
    }
*/
    override fun onCreate() {
        super.onCreate()
    //    baseApplication = this

//        initializeComponent()

    }

/*    private fun initializeComponent() {
        DI.appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .build()
    }*/
}