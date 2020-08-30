package com.ivanasharov.smartplanner.domain

import kotlinx.coroutines.flow.Flow

interface CurrentTasksInteractor {

    fun getCurrentDate(): String
    fun getCurrentDayOfWeek(): Int
    fun getCountTasksAll(): Int?
    fun getCountFinishedTasks(): Int?

   fun getCurrentTasks(): Flow<ArrayList<TaskDomain>>
    fun changeTask(index : Int)
   //suspend fun getCurrentTasks(): Flow<ArrayList<TaskDomain>>
}