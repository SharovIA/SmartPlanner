package com.ivanasharov.smartplanner.data.repositories.calendar

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val mContentResolver: ContentResolver
) : CalendarRepository {

    private val mUri: Uri = CalendarContract.Calendars.CONTENT_URI
    private val mNamesAndIndexsCalendars = HashMap<String, Long>()

    private fun getCalendars() {
        val projection = arrayOf("_id", "calendar_displayName")
        val cur: Cursor? = mContentResolver.query(mUri, projection, null, null, null)
        if (cur != null && cur.moveToFirst()) {
            val nameCol = cur.getColumnIndex(projection[1])
            val idCol = cur.getColumnIndex(projection[0])
            do {
                val name = cur.getString(nameCol)
                val id = cur.getString(idCol)
                mNamesAndIndexsCalendars[name] = id.toLong()
            } while (cur.moveToNext())
            cur.close()
        }
    }

    override fun getAllCalendars(): Flow<List<String>> = flow {
        getCalendars()
        emit(mNamesAndIndexsCalendars.keys.toList())
    }


    override fun insert(event: ContentValues): Boolean {
        val uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, event)
        return uri != null
    }

    override fun getMapOfCalendars(): HashMap<String, Long> = mNamesAndIndexsCalendars
}