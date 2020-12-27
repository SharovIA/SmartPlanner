package com.ivanasharov.smartplanner.data

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

    init {
        getCalendars()
    }

    private fun getCalendars() {
/*        val cur: Cursor? = mContentResolver.query(uri, null, null, null, null)
        val accountNameArrayList = ArrayList<String>()
        while (cur != null && cur.moveToNext()) {
            idList.add(cur.getLong(PROJECTION_ID_INDEX))
            accountNameArrayList.add(cur.getString(PROJECTION_ACCOUNT_NAME_INDEX))
        }
        accountName = accountNameArrayList.toList()*/
        val cur: Cursor? = mContentResolver.query(uri, null, null, null, null)
        while (cur != null && cur.moveToNext()) {
            mNamesAndIndexsCalendars[cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)] = cur.getLong(PROJECTION_ID_INDEX)
        }

    }

    override fun getAllCalendars(): Flow<List<String>> = flow {
        //emit(accountName)
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