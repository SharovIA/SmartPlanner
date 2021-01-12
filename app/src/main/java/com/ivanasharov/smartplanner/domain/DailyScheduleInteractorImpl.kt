package com.ivanasharov.smartplanner.domain

import android.util.Log
import com.ivanasharov.smartplanner.data.NameTimeImportance
import com.ivanasharov.smartplanner.data.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject


class DailyScheduleInteractorImpl @Inject constructor(
    private val taskRepository: TaskRepository
): DailyScheduleInteractor{

    private val calendar = Calendar.getInstance()
    private val date : GregorianCalendar

    init {
        date = GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    companion object{
        private const val MILLIS_IN_MINUTE = 60_000
        private const val MINUTES_IN_HOUR = 60
    }

    override fun getDailySchedule(): Flow<List<NameDurationAndImportanceTask>> = taskRepository.getListCurrentTasksForSchedule(date).map{ list ->
        list.map {
            getDurationAndImportanceTask(it)
        }}


    private fun getDurationAndImportanceTask(nameTimeImportance: NameTimeImportance): NameDurationAndImportanceTask {
        val durationAndImportanceTask = getDuration(nameTimeImportance.timeFrom, nameTimeImportance.timeTo)
        durationAndImportanceTask.importance = nameTimeImportance.importance
        durationAndImportanceTask.name = nameTimeImportance.name
        return durationAndImportanceTask
    }


/*    fun getDuration(timeA: GregorianCalendar, timeB: GregorianCalendar): NameDurationAndImportanceTask{
        var duration = ((timeB.timeInMillis - timeA.timeInMillis)/ MILLIS_IN_MINUTE).toInt()
        val startHours = timeA.get(GregorianCalendar.HOUR_OF_DAY)
        val startMinutes = timeA.get(GregorianCalendar.MINUTE)
        val finishHours = timeB.get(GregorianCalendar.HOUR_OF_DAY)
        val finishMinutes = timeB.get(GregorianCalendar.MINUTE)
        if (startMinutes != 0){
            return getDurationCaseStartMinutesNotNull(duration, startHours, startMinutes, finishHours)
        } else{
            return getDurationCaseStartMinutesNull(duration, startHours, finishHours, finishMinutes)
        }
    }

    private fun getDurationCaseStartMinutesNull(duration: Int, startHours: Int, finishHours: Int, finishMinutes: Int): NameDurationAndImportanceTask {
        val countHours = duration/ MINUTES_IN_HOUR
        val countMinutes = duration% MINUTES_IN_HOUR
        if (countHours!=0 && countMinutes!=0) {
            Log.d("analis", "stat with $startHours -i, 0 min,  + $countHours hours + END to $finishHours hours, $countMinutes min")
            return NameDurationAndImportanceTask(startHours, 0, null, countHours, finishHours, countMinutes, null, null)
        }
        else {
            Log.d("analis", "stat with $startHours -i, 0 min,  + $countHours hours + END to $finishHours hours")
            return NameDurationAndImportanceTask(startHours, 0, null, countHours, countHours, null, null, null)
        }
    }

    private fun getDurationCaseStartMinutesNotNull(duration: Int, startHours: Int, startMinutes: Int, finishHours: Int): NameDurationAndImportanceTask {
        val minutesToEndHour = MINUTES_IN_HOUR - startMinutes
        if (minutesToEndHour >= duration){
            Log.d("analis", "$startHours -i, start $startMinutes, finish ${startMinutes+duration} ")
            return NameDurationAndImportanceTask(startHours, startMinutes, startMinutes+duration, null, null, null, null, null)
        } else{
            val newDuration = duration - minutesToEndHour
            val countHours = newDuration/ MINUTES_IN_HOUR
            val countMinutes = newDuration% MINUTES_IN_HOUR
            if (countMinutes!=0) {
                Log.d("analis", "$startHours -i, start $startMinutes, finish ${MINUTES_IN_HOUR},  + $countHours hours + END $finishHours -i, finish $countMinutes min")
                return NameDurationAndImportanceTask(startHours, startMinutes, MINUTES_IN_HOUR, countHours, finishHours, countMinutes, null, null)
            } else{
                Log.d("analis", "$startHours -i, start $startMinutes, finish ${MINUTES_IN_HOUR},  + $countHours hours + END")
                return NameDurationAndImportanceTask(startHours, startMinutes, MINUTES_IN_HOUR, countHours, null, null, null, null)
            }
        }
    }*/

    fun getDuration(timeA: GregorianCalendar, timeB: GregorianCalendar): NameDurationAndImportanceTask{
        var duration = ((timeB.timeInMillis - timeA.timeInMillis)/ MILLIS_IN_MINUTE).toInt()
        val startHours = timeA.get(GregorianCalendar.HOUR_OF_DAY)
        val startMinutes = timeA.get(GregorianCalendar.MINUTE)
        val finishHours = timeB.get(GregorianCalendar.HOUR_OF_DAY)
        val finishMinutes = timeB.get(GregorianCalendar.MINUTE)
        if (startMinutes != 0){
            return getDurationCaseStartMinutesNotNull(duration, startHours, startMinutes, finishHours)
        } else{
            return getDurationCaseStartMinutesNull(duration, startHours, finishHours, finishMinutes)
        }
    }

    private fun getDurationCaseStartMinutesNull(duration: Int, startHours: Int, finishHours: Int, finishMinutes: Int): NameDurationAndImportanceTask {
        val countHours = duration/ MINUTES_IN_HOUR
        val countMinutes = duration% MINUTES_IN_HOUR
        if (duration < MINUTES_IN_HOUR){
            Log.d("analis", "start $startHours -i, 0 minutes , finish in ${startHours}:${duration} ")
            return NameDurationAndImportanceTask(true, true, true, false,  startHours, 0, duration, null, null, null, null, null)
        } else if (countHours!=0 && countMinutes!=0) {
            Log.d("analis", "stat with $startHours -i, 0 min,  + $countHours hours + END to $finishHours hours, $countMinutes min")
            return NameDurationAndImportanceTask(false, false, true, true, startHours, null, null, countHours, finishHours, countMinutes, null, null)
        } else if (countHours==0) {
            Log.d("analis", "stat with $startHours -i, 0 min,  + $countHours hours + END to $finishHours hours, $countMinutes min")
            return NameDurationAndImportanceTask(false, false, true, false, startHours, null, null, countHours, finishHours, countMinutes, null, null)
        }
        else {
            Log.d("analis", "stat with $startHours -i, 0 min,  + $countHours hours + END to $finishHours hours")
            return NameDurationAndImportanceTask(false, false, false, true, startHours, null, null, countHours, finishHours, null, null, null)
        }
    }

    private fun getDurationCaseStartMinutesNotNull(duration: Int, startHours: Int, startMinutes: Int, finishHours: Int): NameDurationAndImportanceTask {
        val minutesToEndHour = MINUTES_IN_HOUR - startMinutes
        if (minutesToEndHour > duration){
            Log.d("analis", "$startHours -i, start $startMinutes, finish ${startMinutes+duration} ")
            return NameDurationAndImportanceTask(true, true, true, false,  startHours, startMinutes, startMinutes+duration, null, null, null, null, null)
        } else if (minutesToEndHour == duration){
            Log.d("analis", "$startHours -i, start $startMinutes, finish ${startMinutes+duration} ")
            return NameDurationAndImportanceTask(true, true, true, false,  startHours, startMinutes, startMinutes+duration-1, null, null, null, null, null)
        }  else{
            val newDuration = duration - minutesToEndHour
            val countHours = newDuration/ MINUTES_IN_HOUR
            val countMinutes = newDuration% MINUTES_IN_HOUR
            if (countMinutes!=0 && countHours != 0) {
                Log.d("analis", "$startHours -i, start $startMinutes, finish ${MINUTES_IN_HOUR},  + $countHours hours + END $finishHours -i, finish $countMinutes min")
                return NameDurationAndImportanceTask(false, true, true,true, startHours, startMinutes, null, countHours, finishHours, countMinutes, null, null)
            } else if (countMinutes!=0) {
                Log.d("analis", "$startHours -i, start $startMinutes, finish ${MINUTES_IN_HOUR},  + $countHours hours + END $finishHours -i, finish $countMinutes min")
                return NameDurationAndImportanceTask(false,true, true, false, startHours, startMinutes, null, countHours, finishHours, countMinutes, null, null)
            } else{
                Log.d("analis", "$startHours -i, start $startMinutes, finish ${MINUTES_IN_HOUR},  + $countHours hours + END $finishHours -i, finish $countMinutes min")
                return NameDurationAndImportanceTask(false,true, false, true, startHours, startMinutes, null, countHours, finishHours, countMinutes, null, null)
            }
        }
    }


    //val timeA = GregorianCalendar(2021, 0, 5, 9, 40)
    //val timeB = GregorianCalendar(2021, 0, 5, 11, 15)

/*    fun analiz(timeA: GregorianCalendar, timeB: GregorianCalendar){
        var duration = ((timeB.timeInMillis - timeA.timeInMillis)/60000).toInt()
        val startHoursA = timeA.get(GregorianCalendar.HOUR_OF_DAY)
        val startMinutesA = timeA.get(GregorianCalendar.MINUTE)
        val startHoursB = timeB.get(GregorianCalendar.HOUR_OF_DAY)
        val startMinutesB = timeB.get(GregorianCalendar.MINUTE)
        if (startMinutesA != 0){
            val minutesToEndHour =60 - startMinutesA
            if (minutesToEndHour >= duration){
                Log.d("analis", "$startHoursA -i, start $startMinutesA, finish ${startMinutesA+duration} ")
            } else{
                val newDuration = duration - minutesToEndHour
                val countHours = newDuration/60
                val countMinutes = newDuration%60
                val endHour = 60
                if (countMinutes!=0)
                    Log.d("analis", "$startHoursA -i, start $startMinutesA, finish ${endHour},  + $countHours hours + END $startHoursB -i, finish $countMinutes min")
                else
                    Log.d("analis", "$startHoursA -i, start $startMinutesA, finish ${endHour},  + $countHours hours + END")

            }
        } else{

                val countHours = duration/60
                val countMinutes = duration%60
                val endHour = 60
                if (countHours!=0 && countMinutes!=0)
                    Log.d("analis", "stat with $startHoursA -i, 0 min,  + $countHours hours + END to $startHoursB hours, $countMinutes min")
                else if (countHours!=0)
                    Log.d("analis", "stat with $startHoursA -i, 0 min,  + $countHours hours + END to $startHoursB hours")
                else
                    Log.d("analis", "stat with $startHoursA -i, 0 min,  + END to $startMinutesB hours +  $countMinutes hours")
            }
        }*/
}