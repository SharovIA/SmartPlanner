package com.ivanasharov.smartplanner.utils.validators

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import javax.inject.Inject

class InputTaskValidator @Inject constructor(
    private val mResources: ResourceProvider
) {

    fun checkInput(name: String?, date: String?,
                        timeFrom: String?, timeTo: String?):MutableMap<Int, String> {
        val listErrors = mutableMapOf<Int, String>()
        if(name.isNullOrEmpty())
            listErrors[0] = mResources.string(R.string.field) +" "+ mResources.string(R.string.add_task_name) + " "+ mResources.string(R.string.is_empty)
        if(date.isNullOrEmpty())
            listErrors[1] = mResources.string(R.string.field) +" "+ mResources.string(R.string.add_task_date) + " "+ mResources.string(R.string.is_empty)
        if(timeFrom.isNullOrEmpty())
            listErrors[2] = mResources.string(R.string.field) +" "+ mResources.string(R.string.add_task_time_from) + " "+ mResources.string(R.string.is_empty)
        if(timeTo.isNullOrEmpty())
            listErrors[3] = mResources.string(R.string.field) +" "+ mResources.string(R.string.add_task_time_to) + " "+ mResources.string(R.string.is_empty)
        return listErrors
    }
}