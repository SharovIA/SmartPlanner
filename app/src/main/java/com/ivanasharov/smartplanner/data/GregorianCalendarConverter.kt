package com.ivanasharov.smartplanner.data

import androidx.room.TypeConverter
import java.util.*

class GregorianCalendarConverter {

    @TypeConverter
    fun calendarToLong(calendar : GregorianCalendar?) : Long? = calendar?.timeInMillis

    @TypeConverter
    fun longToCalendar(timeInMillis : Long) : GregorianCalendar?{
        val calendar : Calendar  = Calendar.getInstance()
        if (timeInMillis !=null) calendar.timeInMillis = timeInMillis
        return GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE))
    }
}