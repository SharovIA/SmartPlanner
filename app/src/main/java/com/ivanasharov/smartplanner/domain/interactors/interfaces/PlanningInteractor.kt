package com.ivanasharov.smartplanner.domain.interactors.interfaces

import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import kotlinx.coroutines.flow.Flow
import java.util.*

interface PlanningInteractor {
    fun getCountTasksAll(): Int
    fun getCountFinishedTasks(): Int
    fun getCurrentTasks(date: GregorianCalendar): Flow<List<IdNameStatus>>
    fun changeTask(index: Int)
}