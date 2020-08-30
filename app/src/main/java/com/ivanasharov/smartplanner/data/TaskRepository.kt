package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.ArrayList

interface TaskRepository {


    fun save (taskDomain: TaskDomain): Long?
    //fun getListCurrentTasks(calendar: Calendar) : ArrayList<TaskDomain>

    //  suspend fun getListCurrentTasks(calendar: Calendar) : ArrayList<TaskDomain>
    fun getListCurrentTasks(calendar: GregorianCalendar) : Flow<ArrayList<TaskDomain>>

    fun changeTask(task: TaskDomain)

}