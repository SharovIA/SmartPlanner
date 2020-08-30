package com.ivanasharov.smartplanner.data

import android.util.Log
import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TaskRepositoryImpl @Inject constructor(
    private val taskDao : TaskDao
) : TaskRepository {

    override fun getListCurrentTasks(date: GregorianCalendar): Flow<ArrayList<TaskDomain>> = taskDao.getByDate(date).map{
        ConvertTaskDataToTaskDomian().convert(it)
    }


    override fun save(taskDomain: TaskDomain): Long? {
        val task : Task = ConvertTaskDomainToTaskData().convert(taskDomain)
        return taskDao.insert(task)
    }

    override fun changeTask(taskDomain: TaskDomain) {
        val task : Task = ConvertTaskDomainToTaskData().convert(taskDomain)
        taskDao.updateTask(task.name, task.timeFrom, task.status)
    }


    //mapping
    //
    //


/*    override fun getAll(): List<Task> {
        return taskDao.getAll()
    }

    override fun getById(id: Long): Task {
        return taskDao.getById(id)
    }

    override fun insert(task: Task) {
        taskDao.insert(task)
    }

    override fun update(task: Task) {
        taskDao.update(task)
    }

    override fun delete(task: Task) {
        taskDao.delete(task)
    }*/
}