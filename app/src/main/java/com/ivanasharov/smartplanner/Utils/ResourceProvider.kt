package com.ivanasharov.smartplanner.Utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

interface ResourceProvider {
    fun string(@StringRes id : Int): String
    fun array(@ArrayRes id : Int): Array<String>
    fun color(@StringRes id: Int): Int
}