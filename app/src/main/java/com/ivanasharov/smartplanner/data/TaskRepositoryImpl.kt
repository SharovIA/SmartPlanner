package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.entity.Task
import com.ivanasharov.smartplanner.domain.TaskDomain
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TaskRepositoryImpl @Inject constructor(
    private val taskDao : TaskDao
) : TaskRepository {

    private lateinit var arrayListTaskDomian : ArrayList<TaskDomain>

    override fun getListCurrentTasks(calendar: Calendar): ArrayList<TaskDomain> {
        val arrayListTaskData = taskDao.getAll()
        arrayListTaskDomian = ConvertTaskDataToTaskDomian().convert(arrayListTaskData)
        return arrayListTaskDomian
    }

    override fun save(taskDomain: TaskDomain): Long? {
        val task : Task = ConvertTaskDomainToTaskData().convert(taskDomain)
        return taskDao.insert(task)
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