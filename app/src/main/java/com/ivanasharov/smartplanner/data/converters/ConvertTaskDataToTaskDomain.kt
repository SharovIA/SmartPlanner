package com.ivanasharov.smartplanner.data.converters

import com.ivanasharov.smartplanner.data.database.entity.Task
import com.ivanasharov.smartplanner.domain.model.TaskDomain

class ConvertTaskDataToTaskDomain {

    fun convert(tasksData: Task): TaskDomain {
        return TaskDomain(
            tasksData.name, tasksData.description, tasksData.date,
            tasksData.timeFrom, tasksData.timeTo, tasksData.importance, tasksData.address,
            false, tasksData.contact, false, null, tasksData.status, tasksData.id
        )
    }
}