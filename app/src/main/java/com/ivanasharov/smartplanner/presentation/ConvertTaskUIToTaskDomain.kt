package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import java.util.*
import javax.inject.Inject

class ConvertTaskUIToTaskDomain @Inject constructor(
    private val resources: ResourceProvider
) {

    fun convert(taskUI: TaskUI): TaskDomain {
        val category = convertCategory(taskUI.importance.value)
        val timeFrom = convertTime(taskUI.timeFrom.value,
            taskUI.date.value?.get(Calendar.DAY_OF_MONTH),
            taskUI.date.value?.get(Calendar.MONTH),
            taskUI.date.value?.get(Calendar.YEAR))
        val timeTo = convertTime(taskUI.timeTo.value,
            taskUI.date.value?.get(Calendar.DAY_OF_MONTH),
            taskUI.date.value?.get(Calendar.MONTH),
            taskUI.date.value?.get(Calendar.YEAR))

        return TaskDomain(taskUI.name.value, taskUI.description.value,
            taskUI.date.value, timeFrom,  timeTo,
            category, taskUI.address.value,
            taskUI.isSnapContact.value, taskUI.contact.value,
            taskUI.isAddToCalendar.value, taskUI.status.value as Boolean)
    }

    private fun convertTime(strTime : String?,
        day: Int?, month: Int?, year: Int?): GregorianCalendar? {
        var hour  = 0
        var minutes = 0
        val parts = strTime?.split(":")
        if (parts?.get(0) != null) {
            hour = parts[0].toInt()
            minutes = parts[1].toInt()
        }
        if(year != null && month != null && day != null)
            return GregorianCalendar(year, month, day, hour, minutes)
        return null
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

    private fun getArray(): Array<String> = resources.array(R.array.importance)
     //   DI.appComponent.resources().array(R.array.importance)
}