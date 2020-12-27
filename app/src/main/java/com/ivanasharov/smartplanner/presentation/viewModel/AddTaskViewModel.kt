package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.presentation.ConvertTaskUIToTaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class AddTaskViewModel @ViewModelInject constructor(
    private val resource: ResourceProvider,
    private val mAddTaskInteractor: AddTaskInteractor,
    private val mConvert: ConvertTaskUIToTaskDomain
) : BaseViewModel() {

/*    var day: Int
    var month: Int
    var year: Int
    var hours1: Int
    var minutes1: Int
    var hours2: Int
    var minutes2: Int
    val taskUILiveData: TaskUI = TaskUI()

    init {
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
        hours1 = calendar.get(Calendar.HOUR)
        minutes1 = calendar.get(Calendar.MINUTE)
        hours2 = calendar.get(Calendar.HOUR)
        minutes2 = calendar.get(Calendar.MINUTE)
        taskUILiveData.name.value = null
        taskUILiveData.description.value = null
        taskUILiveData.date.value = null
        taskUILiveData.timeFrom.value = null
        taskUILiveData.timeTo.value = null
        taskUILiveData.importance.value = null
        taskUILiveData.address.value = null
        taskUILiveData.isSnapContact.value = false
        taskUILiveData.contact.value = null
        taskUILiveData.isAddToCalendar.value = false
        taskUILiveData.status.value = false

    }


fun updateFullDate() {
    val date = GregorianCalendar(year, month, day)
    taskUILiveData.date.value = date
}
    fun getFullDate() : String{
      return "$day-$month-$year"
    }

    fun updateFullTimeFrom() {
        taskUILiveData.timeFrom.value = getTextValue(hours1) + ":" + getTextValue(minutes1)
    }

    fun updateFullTimeTo() {
        taskUILiveData.timeTo.value = getTextValue(hours2) + ":" + getTextValue(minutes2)
    }


    private fun getTextValue(number: Int): String {
        when (number < 10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val taskDomain = convert.convert(taskUILiveData)
            addTaskInteractor.execute(taskDomain)
        }

        Log.d("test", "vm")
    }*/
//--------------------------------------------------------------------------------
/*    //чтобы никто не мог изменить
   val taskUI= MutableLiveData<TaskUI>()


    val date = MutableLiveData<String?>()
    val timeFrom = MutableLiveData<String?>()
    val timeTo = MutableLiveData<String?>()

    private val mCalendar = Calendar.getInstance()
    val currentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = mCalendar.get(Calendar.MONTH)
    val currentYear = mCalendar.get(Calendar.YEAR)
    val currentHour = mCalendar.get(Calendar.HOUR)
    val currentMinute = mCalendar.get(Calendar.MINUTE)

    fun setDate(year: Int, month: Int, day: Int) {
      date.value = "$day-${month+1}-$year"
    }

    fun setTime(hours: Int, minutes: Int, isTimeFrom: Boolean) {
        if (isTimeFrom) {
            timeFrom.value = getTextValue(hours) + ":" + getTextValue(minutes)
        } else {
            timeTo.value = getTextValue(hours) + ":" + getTextValue(minutes)
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
       //     val taskDomain = convert.convert(taskUILiveData)
       //     addTaskInteractor.execute(taskDomain)
        }

        Log.d("test", "vm")
    }
*/
//чтобы никто не мог изменить
  //  val taskUI= MutableLiveData(NewTaskUI())
    val isSnapContact : MutableLiveData<Boolean> = MutableLiveData(false)
    val isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData(false)
    val nameOfCalendar = MutableLiveData<String>("")

    private val mIsSave = MediatorLiveData<Boolean>()
    val isSave: LiveData<Boolean>
        get() = mIsSave
    val taskUI = TaskUI()

    private val mNamesCalendarsList = MutableLiveData<List<String>>()
    val namesCalendarsList: LiveData<List<String>>
        get() = mNamesCalendarsList

    private val mCalendar = Calendar.getInstance()
    val currentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = mCalendar.get(Calendar.MONTH)
    val currentYear = mCalendar.get(Calendar.YEAR)
    val currentHour = mCalendar.get(Calendar.HOUR)
    val currentMinute = mCalendar.get(Calendar.MINUTE)


    init {
        loadCalendars()
    }

    private fun loadCalendars() {
        viewModelScope.launch(Dispatchers.IO) {
            mAddTaskInteractor.getCalendars().collect{
                mNamesCalendarsList.postValue(it)
            }
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
            val taskDomain = mConvert.convert(taskUI, isSnapContact.value as Boolean, isAddToCalendar.value as Boolean, nameOfCalendar.value)
           mAddTaskInteractor.execute(taskDomain)
            mIsSave.postValue(true)
        }

        Log.d("test", "vm")
    }



}