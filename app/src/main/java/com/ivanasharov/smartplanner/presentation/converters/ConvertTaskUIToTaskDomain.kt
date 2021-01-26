package com.ivanasharov.smartplanner.presentation.converters

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import com.ivanasharov.smartplanner.domain.model.TaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import java.util.*
import javax.inject.Inject

class ConvertTaskUIToTaskDomain @Inject constructor(
    private val mResources: ResourceProvider
) {
    private var mDay : Int? = null
    private var mMonth : Int? = null
    private var mYear : Int? = null

    fun convert(taskUI: TaskUI, isSnapContact:Boolean, isAddToCalendar:Boolean, nameOfCalendar: String?, id: Long?): TaskDomain {
        val category = convertCategory(taskUI.importance.value)
        val date = convertDate(taskUI.date.value)
        val timeFrom = convertTime(taskUI.timeFrom.value)
        val timeTo = convertTime(taskUI.timeTo.value)

        return TaskDomain(
            taskUI.name.value, taskUI.description.value,
            date, timeFrom, timeTo,
            category, taskUI.address.value,
            isSnapContact, taskUI.contact.value,
            isAddToCalendar, nameOfCalendar, taskUI.status.value as Boolean, id
        )
    }

    private fun convertDate(strDate : String?): GregorianCalendar? {
        val parts = strDate?.split("-")
        if (parts?.get(0) != null) {
            mDay = parts[0].toInt()
            mMonth = parts[1].toInt() - 1
            mYear = parts[2].toInt()
        }
        return if(mYear != null && mMonth != null && mDay != null){
            GregorianCalendar(mYear as Int, mMonth as Int, mDay as Int)
        } else
            null
    }

    private fun convertTime(strTime : String?): GregorianCalendar? {
        var hour: Int? = null
        var minutes: Int? = null
        val parts = strTime?.split(":")
        if (parts?.get(0) != null) {
            hour = parts[0].toInt()
            minutes = parts[1].toInt()
        }
        return if(mYear != null && mMonth != null && mDay != null && hour != null && minutes != null)
            GregorianCalendar(mYear as Int, mMonth as Int, mDay as Int, hour, minutes)
        else null
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

    private fun getArray(): Array<String> = mResources.array(R.array.importance)
}