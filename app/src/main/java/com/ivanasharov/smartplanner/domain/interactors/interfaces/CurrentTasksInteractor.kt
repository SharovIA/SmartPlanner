package com.ivanasharov.smartplanner.domain.interactors.interfaces

import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import kotlinx.coroutines.flow.Flow

interface CurrentTasksInteractor {

    fun getCurrentDate(): String
    fun getCurrentDayOfWeek(): Int
    fun getCountTasksAll(): Int
    fun getCountFinishedTasks(): Int
    fun getCurrentTasks(): Flow<List<IdNameStatus>>
    fun changeTask(index : Int)
}