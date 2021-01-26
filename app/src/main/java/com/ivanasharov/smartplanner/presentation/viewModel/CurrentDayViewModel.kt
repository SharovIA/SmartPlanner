package com.ivanasharov.smartplanner.presentation.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import com.ivanasharov.smartplanner.domain.interactors.interfaces.CurrentTasksInteractor
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrentDayViewModel @ViewModelInject constructor(
    private val mResources: ResourceProvider,
    private val mCurrentTasksInteractor: CurrentTasksInteractor
) : BaseViewModel() {

    private val mTaskList = MutableLiveData<List<IdNameStatus>>()
    private val mStatusOfTasks = MutableLiveData<String>()
    private val mDate = MutableLiveData<String>()
    private val mIsLoading = MutableLiveData<Boolean>(true)
    private val mHasTasks = MutableLiveData<Boolean>(false)

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


    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mHasTasks.postValue(false)
            mCurrentTasksInteractor.getCurrentTasks().collect {
                mTaskList.postValue(it)
                mStatusOfTasks.postValue(getStatusOfTasks())
                mDate.postValue(getDate())
                mIsLoading.postValue(false)
            }
        }
    }

    private fun getStatusOfTasks(): String = mResources.string(R.string.completed) + " " +
            "${getCountFinishedTasks()}/${getCountTasks()} " + mResources.string(R.string.completedTasks)

    private fun getCountFinishedTasks(): Int = mCurrentTasksInteractor.getCountFinishedTasks()

    private fun getCountTasks(): Int {
        val count = mCurrentTasksInteractor.getCountTasksAll()
        if (count > 0)
            mHasTasks.postValue(true)
        return count
    }

    private fun getDate(): String {
        val day = mCurrentTasksInteractor.getCurrentDayOfWeek()
        val nameOfDay = getNameDay(day)
        val currentDate = mCurrentTasksInteractor.getCurrentDate()

        return "$nameOfDay, $currentDate"
    }

    private fun getNameDay(day: Int): String {
        when (day) {
            1 -> return mResources.string(R.string.sunday)
            2 -> return mResources.string(R.string.monday)
            3 -> return mResources.string(R.string.tuesday)
            4 -> return mResources.string(R.string.wednesday)
            5 -> return mResources.string(R.string.thursday)
            6 -> return mResources.string(R.string.friday)
            7 -> return mResources.string(R.string.saturday)
            else -> return "Day of the week is undefined"
        }
    }

    fun changeStatus(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mCurrentTasksInteractor.changeTask(index)
        }
    }
}
