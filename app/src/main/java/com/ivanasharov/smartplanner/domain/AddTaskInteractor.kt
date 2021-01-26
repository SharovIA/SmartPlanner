package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.Utils.statuscode.TaskStatusCode
import kotlinx.coroutines.flow.Flow

interface AddTaskInteractor {

    //fun execute(task : TaskDomain)

    fun execute(task : TaskDomain): TaskStatusCode

    fun getCalendars(): Flow<List<String>>

    fun loadTask(id: Long): Flow<TaskDomain>

    fun getContacts(): Flow<List<Contact>>
}