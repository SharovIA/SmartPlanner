package com.ivanasharov.smartplanner.Utils.validators

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import javax.inject.Inject

class InputTaskValidator @Inject constructor(
    private val resources: ResourceProvider
) {

    fun checkInput(name: String?, date: String?,
                        timeFrom: String?, timeTo: String?):MutableMap<Int, String> {
        val listErrors = mutableMapOf<Int, String>()
        if(name.isNullOrEmpty())
            listErrors[0] = resources.string(R.string.field) +" "+ resources.string(R.string.add_task_name) + " "+ resources.string(R.string.is_empty)
        if(date.isNullOrEmpty())
            listErrors[1] = resources.string(R.string.field) +" "+ resources.string(R.string.add_task_date) + " "+ resources.string(R.string.is_empty)
        if(timeFrom.isNullOrEmpty())
            listErrors[2] = resources.string(R.string.field) +" "+ resources.string(R.string.add_task_time_from) + " "+ resources.string(R.string.is_empty)
        if(timeTo.isNullOrEmpty())
            listErrors[3] = resources.string(R.string.field) +" "+ resources.string(R.string.add_task_time_to) + " "+ resources.string(R.string.is_empty)
        return listErrors
    }
}