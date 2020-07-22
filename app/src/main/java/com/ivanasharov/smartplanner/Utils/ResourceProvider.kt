package com.ivanasharov.smartplanner.Utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun string(@StringRes id : Int): String
    fun array(@ArrayRes id : Int): Array<String>
   // fun getContext(): Context
}