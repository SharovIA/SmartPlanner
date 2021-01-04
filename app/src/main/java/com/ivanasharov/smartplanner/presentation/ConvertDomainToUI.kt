package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskUILoad
import java.util.*
import javax.inject.Inject

class ConvertDomainToUI @Inject constructor(
    private val resources: ResourceProvider
) {

/*    fun convert(taskDomain: TaskDomain): TaskUI {
        val taskUI = TaskUI()
        taskUI.name.postValue(taskDomain.name)
        taskUI.description.postValue( taskDomain.description)
        taskUI.date.postValue(convertDate(taskDomain.date))
        taskUI.timeFrom.postValue(convertTime(taskDomain.timeFrom))
        taskUI.timeTo.postValue(convertTime( taskDomain.timeTo))
        taskUI.importance.postValue(ConvertImportance(taskDomain.importance))
        taskUI.address.postValue( taskDomain.address)
        taskUI.contact.postValue( taskDomain.contact)
        taskUI.status.postValue( taskDomain.status)
        return taskUI
    }*/
fun taskDomainToTaskUILoad(taskDomain: TaskDomain): TaskUILoad {
    return TaskUILoad(taskDomain.name as String, taskDomain.description,
    convertDate(taskDomain.date), convertTime(taskDomain.timeFrom),
    convertTime( taskDomain.timeTo), ConvertImportance(taskDomain.importance),
    taskDomain.address, taskDomain.contact,  taskDomain.status, taskDomain.id)
}

    fun taskDomainToTaskUI(taskDomain: TaskDomain): TaskUI {
        val taskUI = TaskUI()
        taskUI.name.postValue(taskDomain.name)
        taskUI.description.postValue( taskDomain.description)
        taskUI.date.postValue(convertDate(taskDomain.date))
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

    private fun getArray(): Array<String> = resources.array(R.array.importance)
    //    DI.appComponent.resources().array(R.array.importance)

    private fun convertTime(calendar: GregorianCalendar?): String {
        val hours = calendar?.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar?.get(Calendar.MINUTE)
        return "${getTextValue(hours)}:${getTextValue(minutes)}"
    }

    private fun convertDate(calendar: GregorianCalendar?): String {
        val year = calendar?.get(Calendar.YEAR)
        val month = calendar?.get(Calendar.MONTH)
        val dayOfMonth = calendar?.get(Calendar.DAY_OF_MONTH)
        return "${getTextValue(dayOfMonth)}-${getTextValue(month as Int  +1)}-${getTextValue(year)}"
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
