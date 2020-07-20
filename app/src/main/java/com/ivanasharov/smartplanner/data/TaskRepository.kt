package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.entity.Task

interface TaskRepository {

    fun getAll(): List<Task>

    fun getById(id : Long): Task

    fun insert(task: Task)

    fun update(task: Task)

    fun delete(task: Task)
}