package com.ivanasharov.smartplanner.domain.interactors

import android.content.ContentValues
import android.provider.CalendarContract
import com.ivanasharov.smartplanner.shared.model.Contact
import com.ivanasharov.smartplanner.utils.statuscode.TaskStatusCode
import com.ivanasharov.smartplanner.data.repositories.calendar.CalendarRepository
import com.ivanasharov.smartplanner.data.repositories.contacts.ContactRepository
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import com.ivanasharov.smartplanner.domain.model.TaskDomain
import com.ivanasharov.smartplanner.domain.interactors.interfaces.AddTaskInteractor
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class AddTaskInteractorImpl @Inject constructor(
    private val mTaskRepository: TaskRepository,
    private val mCalendarRepository: CalendarRepository,
    private val mContactRepository: ContactRepository
) : AddTaskInteractor {

    private var mIsSave: Boolean = false
    private var mIsAddToCalendar: Boolean = false

    override fun execute(task: TaskDomain): TaskStatusCode {

        var isSaveTask: Long? = null
        if (task.id == null) {
            isSaveTask = saveTask(task)
            mIsSave = isSaveTask != null
        } else {
            updateTask(task)
            mIsSave = true
        }

        if (task.isAddCalendar && isSaveTask != null) {
            mIsAddToCalendar = addToCalendar(task)
        }
        return getStatusCode(task.isAddCalendar)
    }

    private fun getStatusCode(addCalendar: Boolean?): TaskStatusCode {
        if (addCalendar != null && !addCalendar) {
            if (mIsSave)
                return TaskStatusCode.SAVED
            else
                return TaskStatusCode.SAVE_ERROR
        } else {
            if (mIsAddToCalendar && mIsSave)
                return TaskStatusCode.SAVED_AND_ADDED_TO_THE_CALENDAR
            else
                return TaskStatusCode.SAVED_BUT_ERROR_ADD_TO_CALENDAR
        }
    }

    private fun updateTask(task: TaskDomain) {
        return mTaskRepository.updateTask(task)
    }

    override fun getCalendars(): Flow<List<String>> = mCalendarRepository.getAllCalendars()

    override fun loadTask(id: Long): Flow<TaskDomain> = mTaskRepository.getTaskById(id)

    override fun getContacts(): Flow<List<Contact>> = mContactRepository.getAllContacts()

    private fun saveTask(task: TaskDomain): Long? {
        return mTaskRepository.save(task)
    }

    private fun addToCalendar(task: TaskDomain): Boolean {
        val date = Calendar.getInstance()
        val dataForCalendar = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, task.timeFrom?.timeInMillis)
            put(CalendarContract.Events.DTEND, task.timeTo?.timeInMillis)
            put(CalendarContract.Events.TITLE, task.name)
            put(CalendarContract.Events.DESCRIPTION, task.description)
            put(
                CalendarContract.Events.CALENDAR_ID,
                mCalendarRepository.getMapOfCalendars()[task.selectNameOfCalendar]
            )
            if (task.address != null)
                put(CalendarContract.Events.EVENT_LOCATION, task.address)
            put(CalendarContract.Events.EVENT_TIMEZONE, date.timeZone.id)
        }
        return mCalendarRepository.insert(dataForCalendar)
    }
}

