package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.Model.TaskUI

class ConvertTaskUIToTaskDomain(
    private val taskUI: TaskUI
) {

    fun convert(): TaskDomain {
        val category = convertCategory(taskUI.importance.value)
        return TaskDomain(taskUI.name.value, taskUI.description.value,
            taskUI.date.value, taskUI.timeFrom.value, taskUI.timeTo.value,
            category, taskUI.address.value, taskUI.isShowMap.value,
            taskUI.isSnapContact.value, taskUI.contact.value,
            taskUI.isAddToCalendar.value)
    }


    private fun convertCategory(category: String?): Int {
        val categories = getArray()
        when(category){
            categories[4] ->  return 1
            categories[3] ->  return 2
            categories[2] ->  return 3
            categories[1] ->  return 4
            else -> return  0
        }
    }

    private fun getArray(): Array<String> =
        DI.appComponent.resources().array(R.array.importance)
}