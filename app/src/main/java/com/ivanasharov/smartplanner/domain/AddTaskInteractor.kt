package com.ivanasharov.smartplanner.domain

import kotlinx.coroutines.flow.Flow

interface AddTaskInteractor {

    fun execute(task : TaskDomain)

    fun getCalendars(): Flow<List<String>>
}