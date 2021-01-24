package com.ivanasharov.smartplanner.domain

import android.content.ContentValues
import android.provider.CalendarContract
import android.util.Log
import com.ivanasharov.smartplanner.Contact
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

    override fun execute(task: TaskDomain) {
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
        //validator 2

        //saveAddress
//        val isSaveAddress: Boolean = saveAddress(task)


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
        val st = mCalendarRepository.insert(dataForCalendar)
        Log.d("run", "vcxz")
        return false
    }


}

