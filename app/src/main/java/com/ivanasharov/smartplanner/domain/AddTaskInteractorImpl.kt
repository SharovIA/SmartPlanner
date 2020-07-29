package com.ivanasharov.smartplanner.domain

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.data.TaskRepository
import com.ivanasharov.smartplanner.presentation.ConvertTaskUIToTaskDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//1. addContact, return contacts

//2. execute:
//validator 1
//saveTask and return id      //addCalendar
//isAddress and validator 2
//saveAddress


class AddTaskInteractorImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : AddTaskInteractor {

    override fun execute(task: TaskDomain) {
        //validator 1
        Log.d("test", "execute")
        //saveTask and return id      //addCalendar
        if (task.isAddCalendar!!) {
            val isAddCalendar: Boolean = addCalendar(task)
        }
        val isSaveTask: Long? = saveTask(task)

        //validator 2

        //saveAddress
//        val isSaveAddress: Boolean = saveAddress(task)


    }

    private fun saveAddress(task: TaskDomain): Boolean {
        //TODO
        return false
    }

    private fun saveTask(task: TaskDomain): Long? {
        return taskRepository.save(task)
    }

    private fun addCalendar(task: TaskDomain): Boolean {
        //TODO
        return false
    }


}

