package com.ivanasharov.smartplanner.domain

import kotlinx.coroutines.flow.Flow
import java.util.*

interface PlanningInteractor {
    fun getCountTasksAll(): Int
    fun getCountFinishedTasks(): Int
    fun getCurrentTasks(date: GregorianCalendar): Flow<List<TaskDomain>>
    fun changeTask(index : Int)
}