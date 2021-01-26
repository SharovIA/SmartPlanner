package com.ivanasharov.smartplanner.utils.resources

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(
    @ApplicationContext private val mContext: Context
): ResourceProvider {
    override fun array(id: Int): Array<String> = mContext.resources.getStringArray(id)

    override fun color(@ColorRes id:  Int): Int = ContextCompat.getColor(mContext, id)

    override fun string(id: Int): String = mContext.resources.getString(id)

}