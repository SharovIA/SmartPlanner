package com.ivanasharov.smartplanner.domain

import kotlinx.coroutines.flow.Flow

interface DailyScheduleInteractor {

    fun getDailySchedule(): Flow<List<NameDurationAndImportanceTask>>
}