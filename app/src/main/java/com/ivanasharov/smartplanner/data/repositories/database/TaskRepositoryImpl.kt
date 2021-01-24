package com.ivanasharov.smartplanner.data.repositories.database

import com.ivanasharov.smartplanner.data.ConvertTaskDataToTaskDomian
import com.ivanasharov.smartplanner.data.ConvertTaskDomainToTaskData
import com.ivanasharov.smartplanner.data.NameTimeImportance
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao : TaskDao
) : TaskRepository {

/*    override fun getListCurrentTasks(date: GregorianCalendar): Flow<ArrayList<TaskDomain>> = taskDao.getByDate(date).map{
        ConvertTaskDataToTaskDomian().convert(it)
    }*/

    override fun getListCurrentTasks(date: GregorianCalendar): Flow<List<TaskDomain>> = taskDao.getByDate(date).map{ list ->
        list.map{
            ConvertTaskDataToTaskDomian().convert(it)
        }
    }

    override fun getListCurrentTasksForSchedule(date: GregorianCalendar): Flow<List<NameTimeImportance>> = taskDao.getByDateForSchedule(date)


    override fun save(taskDomain: TaskDomain): Long? {

        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        return taskDao.insert(task)
    }

    override fun changeStatusTask(taskDomain: TaskDomain) {
        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        taskDao.updateTask(task.name, task.timeFrom, task.status)
    }

    override fun updateTask(taskDomain: TaskDomain) {
        val task : Task = ConvertTaskDomainToTaskData()
            .convert(taskDomain)
        taskDao.update(task)
    }

    override fun getTaskById(id: Long): Flow<TaskDomain> = taskDao.getById(id).map{
        ConvertTaskDataToTaskDomian().convert(it)
    }
}