package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.data.IdNameStatus
import kotlinx.coroutines.flow.Flow

interface CurrentTasksInteractor {

    fun getCurrentDate(): String
    fun getCurrentDayOfWeek(): Int
    fun getCountTasksAll(): Int
    fun getCountFinishedTasks(): Int
   //fun getCurrentTasks(): Flow<List<TaskDomain>>
    fun getCurrentTasks(): Flow<List<IdNameStatus>>
    fun changeTask(index : Int)
}