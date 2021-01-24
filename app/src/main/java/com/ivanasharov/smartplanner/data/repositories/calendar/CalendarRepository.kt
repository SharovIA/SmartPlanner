package com.ivanasharov.smartplanner.data.repositories.calendar

import android.content.ContentValues
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    fun getAllCalendars(): Flow<List<String>>

    fun insert(event: ContentValues): Boolean

    fun getMapOfCalendars(): HashMap<String, Long>
}