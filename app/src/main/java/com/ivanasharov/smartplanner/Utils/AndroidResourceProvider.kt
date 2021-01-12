package com.ivanasharov.smartplanner.Utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

//inject, чтобы сюда пришел контекст
class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
): ResourceProvider {
    override fun array(id: Int): Array<String> = context.resources.getStringArray(id)

    override fun color(@ColorRes id:  Int): Int = ContextCompat.getColor(context, id)

    override fun string(id: Int): String = context.resources.getString(id)

}