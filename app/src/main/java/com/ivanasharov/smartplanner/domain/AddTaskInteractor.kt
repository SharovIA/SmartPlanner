package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.Contact
import kotlinx.coroutines.flow.Flow

interface AddTaskInteractor {

    fun execute(task : TaskDomain)

    fun getCalendars(): Flow<List<String>>

    fun loadTask(id: Long): Flow<TaskDomain>

    fun getContacts(): Flow<List<Contact>>
}