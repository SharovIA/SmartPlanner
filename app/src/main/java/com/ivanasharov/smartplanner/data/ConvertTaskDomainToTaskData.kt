package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import java.util.*

class ConvertTaskDomainToTaskData{

    fun convert(taskDomian: TaskDomain): Task{
        return Task(null,
            taskDomian.name, taskDomian.description, taskDomian.date,
            taskDomian.timeFrom, taskDomian.timeTo, taskDomian.importance,
            false, getAddress(), taskDomian.isShowMap, taskDomian.contact, false)

    }

    private fun getAddress(): Task.Address {
        val address = Task.Address()
        return address
    }
}