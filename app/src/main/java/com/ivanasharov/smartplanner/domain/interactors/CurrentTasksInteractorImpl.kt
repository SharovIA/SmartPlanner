package com.ivanasharov.smartplanner.domain.interactors

import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import com.ivanasharov.smartplanner.domain.interactors.interfaces.CurrentTasksInteractor
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class CurrentTasksInteractorImpl @Inject constructor(
    private val mTaskRepository: TaskRepository
) : CurrentTasksInteractor {

    private val mCalendar = Calendar.getInstance()
    private val mDate : GregorianCalendar
    private lateinit var mTasksOfCurrentDay :List<IdNameStatus>


    init {
      mDate = GregorianCalendar(mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun getCurrentDayOfWeek(): Int {
        return mCalendar.get(Calendar.DAY_OF_WEEK)
    }

    override fun getCurrentDate(): String {
        return "${getTextValue(mCalendar.get(Calendar.DAY_OF_MONTH))}-" +
                "${getTextValue(mCalendar.get(Calendar.MONTH) + 1)}-" +
                "${mCalendar.get(Calendar.YEAR)}"
    }

    override fun getCountTasksAll(): Int {
        return mTasksOfCurrentDay.size
    }

    override fun getCountFinishedTasks(): Int {
        var count = 0
        mTasksOfCurrentDay.forEach {
            if (it.status)
                count++
        }
        return count
    }

    override fun getCurrentTasks(): Flow<List<IdNameStatus>> = mTaskRepository.getListCurrentTasks(mDate).map {
        this.mTasksOfCurrentDay = it
        it
    }

    private fun getTextValue(number: Int): String {
        when (number < 10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }

    override fun changeTask(index: Int) {
        val task = mTasksOfCurrentDay[index]
        task.status = !task.status
        mTaskRepository.changeTaskStatus(task)
    }

}