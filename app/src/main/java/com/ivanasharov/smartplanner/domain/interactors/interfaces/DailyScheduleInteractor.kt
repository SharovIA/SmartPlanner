package com.ivanasharov.smartplanner.domain.interactors.interfaces

import com.ivanasharov.smartplanner.domain.model.NameDurationAndImportanceTask
import kotlinx.coroutines.flow.Flow

interface DailyScheduleInteractor {

    fun getDailySchedule(): Flow<List<NameDurationAndImportanceTask>>
}