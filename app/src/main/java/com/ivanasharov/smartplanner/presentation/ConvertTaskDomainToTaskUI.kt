package com.ivanasharov.smartplanner.presentation

import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.Model.TaskUI

class ConvertTaskDomainToTaskUI {

    fun convert(taskDomain: TaskDomain): TaskUI {
        var taskUI = TaskUI()
        taskUI.name.postValue(taskDomain.name)
        taskUI.description.postValue( taskDomain.description)
        taskUI.date.postValue(taskDomain.date)
       // taskUI.timeFrom.value = taskDomain.timeFrom
       // taskUI.timeTo.value = taskDomain.timeTo
        taskUI.address.postValue( taskDomain.address)
        taskUI.isShowMap.postValue( taskDomain.isShowMap)
        taskUI.status.postValue( taskDomain.status)
        return taskUI
    }
}