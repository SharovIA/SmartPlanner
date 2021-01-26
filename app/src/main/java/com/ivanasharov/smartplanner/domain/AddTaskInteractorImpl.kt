package com.ivanasharov.smartplanner.domain

import android.content.ContentValues
import android.provider.CalendarContract
import android.util.Log
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.Utils.statuscode.TaskStatusCode
import com.ivanasharov.smartplanner.data.repositories.calendar.CalendarRepository
import com.ivanasharov.smartplanner.data.repositories.contacts.ContactRepository
import com.ivanasharov.smartplanner.data.repositories.database.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

//1. addContact, return contacts

//2. execute:
//validator 1
//saveTask and return id      //addCalendar
//isAddress and validator 2
//saveAddress


class AddTaskInteractorImpl @Inject constructor(
    private val mTaskRepository: TaskRepository,
    private val mCalendarRepository: CalendarRepository,
    private val mContactRepository: ContactRepository
) : AddTaskInteractor {

/*    override fun execute(task: TaskDomain) {
        //validator 1
        Log.d("test", "execute")
        //saveTask and return id      //addCalendar
        var isSaveTask: Long? = null
        if (task.id == null) 
             isSaveTask= saveTask(task)
        else
           updateTask(task)
        
        if (task.isAddCalendar != null && task.isAddCalendar && isSaveTask != null) {
            val isAddCalendar: Boolean = addCalendar(task)
        }
    }*/
    private var mIsSave : Boolean = false
    private var mIsAddToCalendar: Boolean = false

override fun execute(task: TaskDomain):TaskStatusCode {
    //validator 1
    Log.d("test", "execute")
    //saveTask and return id      //addCalendar
    var isSaveTask: Long? = null
    if (task.id == null) {
        isSaveTask = saveTask(task)
        mIsSave = isSaveTask != null
    }
    else {
        updateTask(task)
        mIsSave = true
    }

    if (task.isAddCalendar && isSaveTask != null) {
        mIsAddToCalendar = addCalendar(task)
    }
    return getStatusCode(task.isAddCalendar)
}

    private fun getStatusCode(addCalendar: Boolean?): TaskStatusCode {
        if (addCalendar != null && !addCalendar){
            if (mIsSave)
                return TaskStatusCode.SAVED
            else
                return TaskStatusCode.SAVE_ERROR
        } else{
            if(mIsAddToCalendar && mIsSave)
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

    private fun saveAddress(task: TaskDomain): Boolean {
        //TODO
        return false
    }

    private fun saveTask(task: TaskDomain): Long? {
        return mTaskRepository.save(task)
    }

    private fun addCalendar(task: TaskDomain): Boolean {
        val date = Calendar.getInstance()
        val dataForCalendar = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, task.timeFrom?.timeInMillis)
                put(CalendarContract.Events.DTEND, task.timeTo?.timeInMillis)
                put(CalendarContract.Events.TITLE, task.name)
                put(CalendarContract.Events.DESCRIPTION, task.description)
               put(CalendarContract.Events.CALENDAR_ID,
                   mCalendarRepository.getMapOfCalendars()[task.selectNameOfCalendar]
               )
                if(task.address != null)
                    put(CalendarContract.Events.EVENT_LOCATION, task.address)
                put(CalendarContract.Events.EVENT_TIMEZONE, date.timeZone.id)
            }
        return mCalendarRepository.insert(dataForCalendar)
    }


}

