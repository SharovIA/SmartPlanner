package com.ivanasharov.smartplanner.Utils

import android.content.Context
import javax.inject.Inject

//inject, чтобы сюда пришел контекст
class AndroidResourceProvider @Inject constructor(
    private val context: Context
): ResourceProvider {
    override fun array(id: Int): Array<String> = context.resources.getStringArray(id)

    override fun string(id: Int): String = context.resources.getString(id)
}