package com.ivanasharov.smartplanner.domain.interactors

import com.ivanasharov.smartplanner.data.database.requests_model.NameTimeImportance
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import com.ivanasharov.smartplanner.domain.interactors.interfaces.DailyScheduleInteractor
import com.ivanasharov.smartplanner.domain.model.NameDurationAndImportanceTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject


class DailyScheduleInteractorImpl @Inject constructor(
    private val mTaskRepository: TaskRepository
): DailyScheduleInteractor {

    private val mCalendar = Calendar.getInstance()
    private val mCurrentDate : GregorianCalendar

    companion object{
        private const val MILLIS_IN_MINUTE = 60_000
        private const val MINUTES_IN_HOUR = 60
    }

    init {
        mCurrentDate = GregorianCalendar(mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
    }


    override fun getDailySchedule(): Flow<List<NameDurationAndImportanceTask>> = mTaskRepository.getListCurrentTasksForSchedule(mCurrentDate).map{ list ->
        list.map {
            getDurationAndImportanceTask(it)
        }}


    private fun getDurationAndImportanceTask(nameTimeImportance: NameTimeImportance): NameDurationAndImportanceTask {
        val durationAndImportanceTask = getDuration(nameTimeImportance.timeFrom, nameTimeImportance.timeTo)
        durationAndImportanceTask.importance = nameTimeImportance.importance
        durationAndImportanceTask.name = nameTimeImportance.name
        return durationAndImportanceTask
    }

    fun getDuration(timeA: GregorianCalendar, timeB: GregorianCalendar): NameDurationAndImportanceTask {
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
            return NameDurationAndImportanceTask(
                true,
                true,
                true,
                false,
                startHours,
                0,
                duration,
                null,
                null,
                null,
                null,
                null
            )
        } else if (countHours!=0 && countMinutes!=0) {
            return NameDurationAndImportanceTask(
                false,
                false,
                true,
                true,
                startHours,
                null,
                null,
                countHours,
                finishHours,
                countMinutes,
                null,
                null
            )
        } else if (countHours==0) {
            return NameDurationAndImportanceTask(
                false,
                false,
                true,
                false,
                startHours,
                null,
                null,
                countHours,
                finishHours,
                countMinutes,
                null,
                null
            )
        }
        else {
            return NameDurationAndImportanceTask(
                false,
                false,
                false,
                true,
                startHours,
                null,
                null,
                countHours,
                finishHours,
                null,
                null,
                null
            )
        }
    }

    private fun getDurationCaseStartMinutesNotNull(duration: Int, startHours: Int, startMinutes: Int, finishHours: Int): NameDurationAndImportanceTask {
        val minutesToEndHour = MINUTES_IN_HOUR - startMinutes
        if (minutesToEndHour > duration){
            return NameDurationAndImportanceTask(
                true,
                true,
                true,
                false,
                startHours,
                startMinutes,
                startMinutes + duration,
                null,
                null,
                null,
                null,
                null
            )
        } else if (minutesToEndHour == duration){
            return NameDurationAndImportanceTask(
                true,
                true,
                true,
                false,
                startHours,
                startMinutes,
                startMinutes + duration - 1,
                null,
                null,
                null,
                null,
                null
            )
        }  else{
            val newDuration = duration - minutesToEndHour
            val countHours = newDuration/ MINUTES_IN_HOUR
            val countMinutes = newDuration% MINUTES_IN_HOUR
            if (countMinutes!=0 && countHours != 0) {
               return NameDurationAndImportanceTask(
                    false,
                    true,
                    true,
                    true,
                    startHours,
                    startMinutes,
                    null,
                    countHours,
                    finishHours,
                    countMinutes,
                    null,
                    null
                )
            } else if (countMinutes!=0) {
                return NameDurationAndImportanceTask(
                    false,
                    true,
                    true,
                    false,
                    startHours,
                    startMinutes,
                    null,
                    countHours,
                    finishHours,
                    countMinutes,
                    null,
                    null
                )
            } else{
               return NameDurationAndImportanceTask(
                    false,
                    true,
                    false,
                    true,
                    startHours,
                    startMinutes,
                    null,
                    countHours,
                    finishHours,
                    countMinutes,
                    null,
                    null
                )
            }
        }
    }

}