package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.presentation.ConvertTaskDomainToTaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CurrentDayViewModel @ViewModelInject constructor(
    private val resources: ResourceProvider,
    private val currentTasksInteractor: CurrentTasksInteractor,
    private val convert: ConvertTaskDomainToTaskUI
) : BaseViewModel() {
    // tasks
    // current date
    // count of task

/*
    val currentTasks = MutableLiveData<ArrayList<TaskUI>>()
    val date  = MutableLiveData<String>()
    val statusOfTasks  = MutableLiveData<String>()
    val countTasksAll  : MutableLiveData<Int> = MutableLiveData()
    val countFinishedTasks  : MutableLiveData<Int> = MutableLiveData()

    init {
        getData()
    }

    private fun getData() {
        val tasks = ArrayList<TaskUI>()
        viewModelScope.launch(Dispatchers.IO) {
            currentTasksInteractor.getCurrentTasks().collect{
                if(tasks.size !=0) tasks.clear()
                it.forEach {value ->
                    tasks.add(convert.convert(value))
                }
                currentTasks.postValue(tasks)
                statusOfTasks.postValue("${getCountFinishedTasks()}/${getCountTasks()}")
             //   countTasksAll.postValue( getCountTasks())
              //  countFinishedTasks.postValue(getCountFinishedTasks())
            }
        }
        date.value = getDate()
    }

    private fun getCountFinishedTasks(): Int? =currentTasksInteractor.getCountFinishedTasks()

    private fun getCountTasks(): Int? = currentTasksInteractor.getCountTasksAll()


    private fun getDate(): String {
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

    fun changeStatus(index : Int){
        viewModelScope.launch(Dispatchers.IO) {
            currentTasksInteractor.changeTask(index)
        }
        Log.d("test", "check: ")
    }
*/
//чтобы никто не мог изменить
    private val mTaskList = MutableLiveData<List<TaskViewModel>>()
    private val mStatusOfTasks  = MutableLiveData<String>()
    private val mDate  = MutableLiveData<String>()
    private val mIsLoading = MutableLiveData<Boolean>(true)

    //чтобы можно было получить данные
    val taskList: LiveData<List<TaskViewModel>>
        get()=mTaskList
    val statusOfTasks: LiveData<String>
        get()=mStatusOfTasks
    val date : LiveData<String>
        get() = mDate
    val isLoading: LiveData<Boolean>
        get() = mIsLoading

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            currentTasksInteractor.getCurrentTasks().map{list->
                    list.map { it
                        convert.convert(it)
                    }
                }.collect{
                mTaskList.postValue(it.map{ taskUILoad ->
                        TaskViewModel(taskUILoad)
                    })
                mStatusOfTasks.postValue(getStatusOfTasks())
                mDate.postValue(getDate())
                mIsLoading.postValue(false)
                }
        }
    }

    private fun getStatusOfTasks(): String = resources.string(R.string.completed) +" " +
            "${getCountFinishedTasks()}/${getCountTasks()} " +  resources.string(R.string.completedTasks)

    private fun getCountFinishedTasks(): Int? =currentTasksInteractor.getCountFinishedTasks()

    private fun getCountTasks(): Int? = currentTasksInteractor.getCountTasksAll()


    private fun getDate(): String {
        val day = currentTasksInteractor.getCurrentDayOfWeek()
        val nameOfDay = getNameDay(day)
        val currentDate = currentTasksInteractor.getCurrentDate()

        return "$nameOfDay, $currentDate"
    }

    //неделя по-английски с вс начинается
    private fun getNameDay(day: Int): String {
        when(day){
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

    fun changeStatus(index : Int){
        viewModelScope.launch(Dispatchers.IO) {
            currentTasksInteractor.changeTask(index)
        }
    }
}
