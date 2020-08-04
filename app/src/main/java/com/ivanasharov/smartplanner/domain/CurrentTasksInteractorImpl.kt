package com.ivanasharov.smartplanner.domain

import android.util.Log
import com.ivanasharov.smartplanner.data.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CurrentTasksInteractorImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : CurrentTasksInteractor {

    private val calendar = Calendar.getInstance()
    private lateinit var tasksOfCurrentDay : ArrayList<TaskDomain>

    override fun getCurrentDayOfWeek(): Int {
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    override fun getCurrentDate(): String {
        return "${getTextValue(calendar.get(Calendar.DAY_OF_MONTH))}-" +
                "${getTextValue(calendar.get(Calendar.MONTH) + 1)}-" +
                "${calendar.get(Calendar.YEAR)}"
    }

    override fun getCountTasksAll(): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountFinishedTasks(): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

/*    override suspend fun getCurrentTasks(): ArrayList<TaskDomain> {
        tasksOfCurrentDay = taskRepository.getListCurrentTasks(calendar)
        return tasksOfCurrentDay
    }*/
    override fun getCurrentTasks(): Flow<ArrayList<TaskDomain>> = flow {
    //tasksOfCurrentDay =
    val s = taskRepository.getListCurrentTasks(calendar)
    emit(s)
    //return tasksOfCurrentDay
}

    private fun getTextValue(number: Int): String {
        when (number < 10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }
}