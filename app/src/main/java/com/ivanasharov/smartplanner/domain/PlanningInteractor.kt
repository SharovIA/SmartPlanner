package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.data.IdNameStatus
import kotlinx.coroutines.flow.Flow
import java.util.*

interface PlanningInteractor {
    fun getCountTasksAll(): Int
    fun getCountFinishedTasks(): Int
   // fun getCurrentTasks(date: GregorianCalendar): Flow<List<TaskDomain>>
   fun getCurrentTasks(date: GregorianCalendar): Flow<List<IdNameStatus>>
    fun changeTask(index : Int)
}