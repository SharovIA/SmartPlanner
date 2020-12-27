package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import java.util.*
import javax.inject.Inject

class ConvertTaskUIToTaskDomain @Inject constructor(
    private val resources: ResourceProvider
) {
    private var day : Int? = null
    private var month : Int? = null
    private var year : Int? = null

    fun convert(taskUI: TaskUI, isSnapContact:Boolean, isAddToCalendar:Boolean, nameOfCalendar: String?): TaskDomain {
        val category = convertCategory(taskUI.importance.value)
        val date = convertDate(taskUI.date.value)
        val timeFrom = convertTime(taskUI.timeFrom.value)
        val timeTo = convertTime(taskUI.timeTo.value)

        return TaskDomain(taskUI.name.value, taskUI.description.value,
            date, timeFrom,  timeTo,
            category, taskUI.address.value,
            isSnapContact, taskUI.contact.value,
            isAddToCalendar, nameOfCalendar, taskUI.status.value as Boolean)
    }

    private fun convertDate(strDate : String?): GregorianCalendar? {
        val parts = strDate?.split("-")
        if (parts?.get(0) != null) {
            day = parts[0].toInt()
            month = parts[1].toInt() - 1
            year = parts[2].toInt()
        }
        return if(year != null && month != null && day != null){
            GregorianCalendar(year as Int, month as Int, day as Int)
        } else
            null
    }

/*    private fun convertDate(strDate : String?): GregorianCalendar? {
        var year :Int? =null
        var month :Int? = null
        var day :Int? = null
        val parts = strDate?.split("-")
        if (parts?.get(0) != null) {
            day = parts[0].toInt()
            month = parts[1].toInt()
            year = parts[2].toInt()
        }
        if(year != null && month != null && day != null)
            return GregorianCalendar(year, month, day)
        return null
    }*/

/*    private fun convertTime(strTime : String?,
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
    }*/

    private fun convertTime(strTime : String?): GregorianCalendar? {
        var hour: Int? = null
        var minutes: Int? = null
        val parts = strTime?.split(":")
        if (parts?.get(0) != null) {
            hour = parts[0].toInt()
            minutes = parts[1].toInt()
        }
        return if(year != null && month != null && day != null && hour != null && minutes != null)
            GregorianCalendar(year as Int, month as Int, day as Int, hour, minutes)
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

    private fun getArray(): Array<String> = resources.array(R.array.importance)
     //   DI.appComponent.resources().array(R.array.importance)
}