package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class PlanningInteractorImpl @Inject constructor(
    private val mTaskRepository: TaskRepository
): PlanningInteractor {

    private lateinit var mTasksOfCurrentDay :List<TaskDomain>

    override fun getCountTasksAll(): Int = mTasksOfCurrentDay.size

    override fun getCountFinishedTasks(): Int {
        var count = 0
        mTasksOfCurrentDay.forEach {
            if (it.status)
                count++
        }
        return count
    }

    override fun getCurrentTasks(date: GregorianCalendar): Flow<List<TaskDomain>> = mTaskRepository.getListCurrentTasks(date).map {
        this.mTasksOfCurrentDay = it
        it
    }


    override fun changeTask(index: Int) {
        val task = mTasksOfCurrentDay[index]
        task.status = !task.status
        mTaskRepository.changeStatusTask(task)
    }

}