package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class CurrentTasksInteractorImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : CurrentTasksInteractor {

    private val calendar = Calendar.getInstance()
   private lateinit var tasksOfCurrentDay :List<TaskDomain>
    private val date : GregorianCalendar

    init {
      date = GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun getCurrentDayOfWeek(): Int {
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    override fun getCurrentDate(): String {
        return "${getTextValue(calendar.get(Calendar.DAY_OF_MONTH))}-" +
                "${getTextValue(calendar.get(Calendar.MONTH) + 1)}-" +
                "${calendar.get(Calendar.YEAR)}"
    }

    override fun getCountTasksAll(): Int {
        return tasksOfCurrentDay.size
    }

    override fun getCountFinishedTasks(): Int {
        var count = 0
        tasksOfCurrentDay.forEach {
            if (it.status)
                count++
        }
        return count
    }


    override fun getCurrentTasks(): Flow<List<TaskDomain>> = taskRepository.getListCurrentTasks(date).map {
        this.tasksOfCurrentDay = it
        it
    }


    private fun getTextValue(number: Int): String {
        when (number < 10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }

    override fun changeTask(index: Int) {
        val task = tasksOfCurrentDay[index]
        task.status = !task.status
        taskRepository.changeStatusTask(task)
    }

}