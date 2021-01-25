package com.ivanasharov.smartplanner.data.repositories.database

import com.ivanasharov.smartplanner.data.IdNameStatus
import com.ivanasharov.smartplanner.data.NameTimeImportance
import com.ivanasharov.smartplanner.domain.TaskDomain
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TaskRepository {

    fun save (taskDomain: TaskDomain): Long?

 //   fun getListCurrentTasks(date: GregorianCalendar) : Flow<List<TaskDomain>>
    fun getListCurrentTasks(date: GregorianCalendar) : Flow<List<IdNameStatus>>

    fun getListCurrentTasksForSchedule(date: GregorianCalendar) : Flow<List<NameTimeImportance>>

    fun changeStatusTask(taskDomain: TaskDomain)

    fun updateTask(taskDomain: TaskDomain)

    fun changeTaskStatus(idNameStatus: IdNameStatus)

    fun getTaskById(id: Long): Flow<TaskDomain>

}