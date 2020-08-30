package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.presentation.ConvertTaskUIToTaskDomain
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class AddTaskViewModel @Inject constructor(
    private val resource: ResourceProvider,
    private val addTaskInteractor: AddTaskInteractor
) : BaseViewModel() {

    var day: Int
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
        taskUILiveData.isShowMap.value = false
        taskUILiveData.isSnapContact.value = false
        taskUILiveData.contact.value = null
        taskUILiveData.isAddToCalendar.value = false
        taskUILiveData.status.value = false

    }

/*    fun updateFullDate() {
        taskUILiveData.date.value = "$day-$month-$year"
    }*/
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
            val taskDomain = ConvertTaskUIToTaskDomain(taskUILiveData).convert()
            addTaskInteractor.execute(taskDomain)
        }

        Log.d("test", "vm")
    }

}