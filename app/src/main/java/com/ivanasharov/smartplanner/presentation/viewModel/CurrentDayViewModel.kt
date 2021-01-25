package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.data.IdNameStatus
import com.ivanasharov.smartplanner.data.repositories.RemoteWeatherRepository
import com.ivanasharov.smartplanner.data.repositories.RemoteWeatherRepositoryImpl
import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.DailyScheduleInteractor
import com.ivanasharov.smartplanner.domain.DailyScheduleInteractorImpl
import com.ivanasharov.smartplanner.presentation.ConvertDomainToUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class CurrentDayViewModel @ViewModelInject constructor(
    private val resources: ResourceProvider,
    private val currentTasksInteractor: CurrentTasksInteractor,
    private val convert: ConvertDomainToUI
) : BaseViewModel() {

    //чтобы никто не мог изменить
//    private val mTaskList = MutableLiveData<List<TaskViewModel>>()
    private val mTaskList = MutableLiveData<List<IdNameStatus>>()
    private val mStatusOfTasks = MutableLiveData<String>()
    private val mDate = MutableLiveData<String>()
    private val mIsLoading = MutableLiveData<Boolean>(true)
    private val mHasTasks = MutableLiveData<Boolean>(false)

    //чтобы можно было получить данные
/*    val taskList: LiveData<List<TaskViewModel>>
        get() = mTaskList*/
    val taskList: LiveData<List<IdNameStatus>>
        get() = mTaskList
    val statusOfTasks: LiveData<String>
        get() = mStatusOfTasks
    val date: LiveData<String>
        get() = mDate
    val isLoading: LiveData<Boolean>
        get() = mIsLoading
    val hasTasks: LiveData<Boolean>
        get() = mHasTasks

    init {
        getData()
    }


/*    private fun TEST() {
        val ttt = DailyScheduleInteractorImpl()
        val timeA1 = GregorianCalendar(2021, 0, 5, 9, 40)
        val timeB1 = GregorianCalendar(2021, 0, 5, 11, 15)
        Log.d("analis", "test1")
        ttt.analiz(timeA1, timeB1)

        val timeA2 = GregorianCalendar(2021, 0, 5, 9, 40)
        val timeB2 = GregorianCalendar(2021, 0, 5, 9, 50)
        Log.d("analis", "test2")
        ttt.analiz(timeA2, timeB2)

        val timeA3 = GregorianCalendar(2021, 0, 5, 9, 58)
        val timeB3 = GregorianCalendar(2021, 0, 5, 11, 0)
        Log.d("analis", "test3")
        ttt.analiz(timeA3, timeB3)

        val timeA4 = GregorianCalendar(2021, 0, 5, 0, 0)
        val timeB4 = GregorianCalendar(2021, 0, 5, 11, 15)
        Log.d("analis", "test4")
        ttt.analiz(timeA4, timeB4)

        val timeA5 = GregorianCalendar(2021, 0, 5, 9, 0)
        val timeB5 = GregorianCalendar(2021, 0, 5, 11, 0)
        Log.d("analis", "test5")
        ttt.analiz(timeA5, timeB5)
    }*/

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mHasTasks.postValue(false)
            currentTasksInteractor.getCurrentTasks().collect {
                mTaskList.postValue(it)
                mStatusOfTasks.postValue(getStatusOfTasks())
                mDate.postValue(getDate())
                mIsLoading.postValue(false)
            }
        }
    }


    /* private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mHasTasks.postValue(false)
            currentTasksInteractor.getCurrentTasks().map { list ->
                list.map {
                    it
                    convert.taskDomainToTaskUILoad(it)
                }
            }.collect {
                mTaskList.postValue(it.map { taskUILoad ->
                    TaskViewModel(taskUILoad)
                })
                mStatusOfTasks.postValue(getStatusOfTasks())
                mDate.postValue(getDate())
                mIsLoading.postValue(false)
            }
        }
    }
*/
    private fun getStatusOfTasks(): String = resources.string(R.string.completed) + " " +
            "${getCountFinishedTasks()}/${getCountTasks()} " + resources.string(R.string.completedTasks)

    private fun getCountFinishedTasks(): Int = currentTasksInteractor.getCountFinishedTasks()

    private fun getCountTasks(): Int {
        val count = currentTasksInteractor.getCountTasksAll()
        if (count > 0)
            mHasTasks.postValue(true)
        return count
    }


    private fun getDate(): String {
        val day = currentTasksInteractor.getCurrentDayOfWeek()
        val nameOfDay = getNameDay(day)
        val currentDate = currentTasksInteractor.getCurrentDate()

        return "$nameOfDay, $currentDate"
    }

    //неделя по-английски с вс начинается
    private fun getNameDay(day: Int): String {
        when (day) {
            1 -> return resources.string(R.string.sunday)
            2 -> return resources.string(R.string.monday)
            3 -> return resources.string(R.string.tuesday)
            4 -> return resources.string(R.string.wednesday)
            5 -> return resources.string(R.string.thursday)
            6 -> return resources.string(R.string.friday)
            7 -> return resources.string(R.string.saturday)
            else -> return "Day of the week is undefined"
        }
    }

    fun changeStatus(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentTasksInteractor.changeTask(index)
        }
    }
}
