package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.dao.TaskDao
import com.ivanasharov.smartplanner.data.entity.Task
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao : TaskDao
) : TaskRepository {



    override fun getAll(): List<Task> {
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
    }
}