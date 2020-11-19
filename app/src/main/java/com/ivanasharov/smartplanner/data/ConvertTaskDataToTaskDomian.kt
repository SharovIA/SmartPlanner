package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import kotlin.collections.ArrayList

class ConvertTaskDataToTaskDomian {

    fun convert(arrayListTasksData: List<Task>): ArrayList<TaskDomain> {
        val arrayListTaskDomain = ArrayList<TaskDomain>()
        var taskDomain : TaskDomain

        arrayListTasksData.forEach {
             taskDomain = TaskDomain(it.name, it.description, it.date,
                it.timeFrom, it.timeTo, it.importance, it.address,
                false, it.contact, false, it.status)
            arrayListTaskDomain.add(taskDomain)
        }
        return arrayListTaskDomain

    }
}