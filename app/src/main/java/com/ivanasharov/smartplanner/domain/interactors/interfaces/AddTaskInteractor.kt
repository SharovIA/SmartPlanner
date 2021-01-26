package com.ivanasharov.smartplanner.domain.interactors.interfaces

import com.ivanasharov.smartplanner.shared.model.Contact
import com.ivanasharov.smartplanner.utils.statuscode.TaskStatusCode
import com.ivanasharov.smartplanner.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow

interface AddTaskInteractor {

    fun execute(task : TaskDomain): TaskStatusCode

    fun getCalendars(): Flow<List<String>>

    fun loadTask(id: Long): Flow<TaskDomain>

    fun getContacts(): Flow<List<Contact>>
}