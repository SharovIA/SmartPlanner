package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain

class ConvertTaskDomainToTaskData{

    fun convert(taskDomian: TaskDomain): Task{
        return Task(null,
            taskDomian.name, taskDomian.description, taskDomian.date,
            taskDomian.timeFrom, taskDomian.timeTo, taskDomian.importance,
            false, taskDomian.address, taskDomian.contact, taskDomian.status)

    }
}