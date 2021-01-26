package com.ivanasharov.smartplanner.data.converters

import com.ivanasharov.smartplanner.data.database.entity.Task
import com.ivanasharov.smartplanner.domain.model.TaskDomain

class ConvertTaskDomainToTaskData{

    fun convert(taskDomian: TaskDomain): Task{
        return Task(taskDomian.id,
            taskDomian.name, taskDomian.description, taskDomian.date,
            taskDomian.timeFrom, taskDomian.timeTo, taskDomian.importance,
            false, taskDomian.address, taskDomian.contact, taskDomian.status)

    }
}