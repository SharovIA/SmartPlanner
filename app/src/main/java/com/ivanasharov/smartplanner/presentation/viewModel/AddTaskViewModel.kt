package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.ConvertDomainToUI
import com.ivanasharov.smartplanner.presentation.ConvertTaskUIToTaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*


class AddTaskViewModel @ViewModelInject constructor(
    private val resource: ResourceProvider,
    private val mAddTaskInteractor: AddTaskInteractor,
    private val mConvert: ConvertTaskUIToTaskDomain,
    private val mConvertDomainToUI: ConvertDomainToUI
) : BaseViewModel() {

    private val mIdTask = MutableLiveData<Long?>(null)
    private val mIsSave = MediatorLiveData<Boolean>()
    private val mIsLoading= MutableLiveData<Boolean>(false)
    private val mNamesCalendarsList = MutableLiveData<List<String>>()
    private val mListContacts = MutableLiveData<List<String>>()
    private val mCalendar = Calendar.getInstance()

    var modeNewTask = true //Add New Task mode
    var taskUI = TaskUI()
    val nameOfCalendar = MutableLiveData<String>("")
    val isSnapContact : MutableLiveData<Boolean> = MutableLiveData(false)
    val isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData(false)


    val isSave: LiveData<Boolean>
        get() = mIsSave
    val namesCalendarsList: LiveData<List<String>>
        get() = mNamesCalendarsList
    val listContacts: LiveData<List<String>>
        get() = mListContacts
    val isLoading: LiveData<Boolean>
        get() = mIsLoading

    val currentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = mCalendar.get(Calendar.MONTH)
    val currentYear = mCalendar.get(Calendar.YEAR)
    val currentHour = mCalendar.get(Calendar.HOUR)
    val currentMinute = mCalendar.get(Calendar.MINUTE)


    fun loadTaskForEdit(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            mIdTask.postValue(id)
            mIsLoading.postValue(true)
            mAddTaskInteractor.loadTask(id).collect{
                taskUI = mConvertDomainToUI.taskDomainToTaskUI(it)
                mIsLoading.postValue( false)
            }
        }
    }

    fun loadContacts(){
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mAddTaskInteractor.getContacts().map{list ->
                list.map {
                    it.getData()
                }
            }.collect{
                mListContacts.postValue(it)
                mIsLoading.postValue( false)
            }
        }
    }

    fun loadCalendars() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mAddTaskInteractor.getCalendars().collect{
                mNamesCalendarsList.postValue(it)
            }
            mIsLoading.postValue(false)
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        taskUI.date.value = "$day-${month+1}-$year"
    }

    fun setTime(hours: Int, minutes: Int, isTimeFrom: Boolean) {
        if (isTimeFrom) {
            taskUI.timeFrom.value = getTextValue(hours) + ":" + getTextValue(minutes)
        } else {
            taskUI.timeTo.value = getTextValue(hours) + ":" + getTextValue(minutes)
        }
    }

    private fun getTextValue(number: Int): String {
        when (number < 10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsSave.postValue(false)
            val taskDomain = mConvert.convert(taskUI, isSnapContact.value as Boolean, isAddToCalendar.value as Boolean, nameOfCalendar.value, mIdTask.value)
           mAddTaskInteractor.execute(taskDomain)
            mIsSave.postValue(true)
        }

        Log.d("test", "vm")
    }

}