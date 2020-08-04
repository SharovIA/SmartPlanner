package com.ivanasharov.smartplanner.domain

import kotlinx.coroutines.flow.Flow

interface CurrentTasksInteractor {

    fun getCurrentDate(): String
    fun getCurrentDayOfWeek(): Int
    fun getCountTasksAll(): Int?
    fun getCountFinishedTasks(): Int?
  //  suspend fun getCurrentTasks(): ArrayList<TaskDomain>
   fun getCurrentTasks(): Flow<ArrayList<TaskDomain>>
}