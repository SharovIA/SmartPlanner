package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import java.util.*
import javax.inject.Inject

class ConvertTaskDomainToTaskUI {


    fun convert(taskDomain: TaskDomain): TaskUI {
        var taskUI = TaskUI()
        taskUI.name.postValue(taskDomain.name)
        taskUI.description.postValue( taskDomain.description)
        taskUI.date.postValue(taskDomain.date)
        taskUI.timeFrom.postValue(convertTime(taskDomain.timeFrom))
        taskUI.timeTo.postValue(convertTime( taskDomain.timeTo))
        taskUI.importance.postValue(ConvertImportance(taskDomain.importance))
        taskUI.address.postValue( taskDomain.address)
        taskUI.contact.postValue( taskDomain.contact)
        taskUI.status.postValue( taskDomain.status)
        return taskUI
    }

    private fun ConvertImportance(importance: Int): String? {
        val categories = getArray()
        when(importance){
            1 -> return categories[4]
            2-> return categories[3]
            3 -> return categories[2]
            4 -> return categories[1]
            else -> return ""
        }
    }

    private fun getArray(): Array<String> =
        DI.appComponent.resources().array(R.array.importance)

    private fun convertTime(calendar: GregorianCalendar?): String? {
        val hours = calendar?.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar?.get(Calendar.MINUTE)
        return "${getTextValue(hours)}:${getTextValue(minutes)}"
    }

    private fun getTextValue(number: Int?): String? {
        if (number!=null){
            when (number < 10) {
                true -> return "0$number"
                false -> return "$number"
            }
        }
        else  return null
    }
}
