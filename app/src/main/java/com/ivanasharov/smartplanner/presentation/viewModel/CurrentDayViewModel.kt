package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.TaskDomain
import com.ivanasharov.smartplanner.presentation.ConvertTaskDomainToTaskUI
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CurrentDayViewModel @Inject constructor(
    private val resource: ResourceProvider,
    private val currentTasksInteractor: CurrentTasksInteractor
) : BaseViewModel() {
    // tasks
    // current date
    // count of task

    val currentTasks : MutableLiveData<ArrayList<TaskUI>> = MutableLiveData()
    val date  : MutableLiveData<String> = MutableLiveData()
    val countTasksAll  : MutableLiveData<Int> = MutableLiveData()
    val countFinishedTasks  : MutableLiveData<Int> = MutableLiveData()

    init {
        getData()
    }

    fun getData() {
        val tasks = ArrayList<TaskUI>()
        viewModelScope.launch(Dispatchers.IO) {
            currentTasksInteractor.getCurrentTasks().collect{
                it.forEach {value ->
                    tasks.add(ConvertTaskDomainToTaskUI().convert(value))
                }
                currentTasks.postValue(tasks)
            }
        }
        date.value = getDate()
    }


    private fun getDate(): String? {
        val day = currentTasksInteractor.getCurrentDayOfWeek()
        val nameOfDay = getNameDay(day)
        val currentDate = currentTasksInteractor.getCurrentDate()

        return "$nameOfDay, $currentDate"
    }

    //неделя по-английски с вс начинается
    private fun getNameDay(day: Int): String {
        when(day){
            1 -> return resource.string(R.string.sunday)
            2 -> return resource.string(R.string.monday)
            3 -> return resource.string(R.string.tuesday)
            4 -> return resource.string(R.string.wednesday)
            5 -> return resource.string(R.string.thursday)
            6 -> return resource.string(R.string.friday)
            7 -> return resource.string(R.string.saturday)
            else -> return "Day of the week is undefined"
        }
    }
}
