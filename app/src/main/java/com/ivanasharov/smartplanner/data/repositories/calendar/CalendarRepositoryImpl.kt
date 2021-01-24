package com.ivanasharov.smartplanner.data.repositories.calendar

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val mContentResolver: ContentResolver
) : CalendarRepository {

    private val uri: Uri = CalendarContract.Calendars.CONTENT_URI
    private val idList = ArrayList<Long>()
    private val mNamesAndIndexsCalendars = HashMap<String, Long>()
    private lateinit var accountName: List<String>

    companion object {
        private const val PROJECTION_ID_INDEX: Int = 0
        private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    }

/*    private fun getCalendars() {

        val cur: Cursor? = mContentResolver.query(uri, null, null, null, null)
        while (cur != null && cur.moveToNext()) {
            if(cur.getString(PROJECTION_ACCOUNT_NAME_INDEX) != null)
         //   val a = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
           // val b  = cur.getLong(PROJECTION_ID_INDEX)
            mNamesAndIndexsCalendars[cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)] = cur.getLong(PROJECTION_ID_INDEX)
        }
        Log.d("r", "fds")

    }*/

    private fun getCalendars() {
        val projection = arrayOf("_id", "calendar_displayName")

        val cur: Cursor? = mContentResolver.query(uri, projection, null, null, null)
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

        Log.d("r", "fds")

    }

    override fun getAllCalendars(): Flow<List<String>> = flow {
        getCalendars()
        emit(mNamesAndIndexsCalendars.keys.toList())
    }


    override fun insert(values: ContentValues): Boolean {


        //val calID = CalendarContract.Calendars
        val uri0 = CalendarContract.Calendars.CONTENT_URI
        val uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventID = uri?.lastPathSegment?.toLong()
        Log.d("vcxz", "ga")
        return false
    }

    override fun getMapOfCalendars(): HashMap<String, Long> = mNamesAndIndexsCalendars
}