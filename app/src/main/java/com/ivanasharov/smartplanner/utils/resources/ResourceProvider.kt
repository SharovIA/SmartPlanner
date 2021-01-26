package com.ivanasharov.smartplanner.utils.resources

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun string(@StringRes id : Int): String
    fun array(@ArrayRes id : Int): Array<String>
    fun color(@StringRes id: Int): Int
}