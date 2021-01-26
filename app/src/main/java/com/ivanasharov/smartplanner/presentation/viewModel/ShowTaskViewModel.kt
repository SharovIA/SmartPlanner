package com.ivanasharov.smartplanner.presentation.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.domain.interactors.interfaces.AddTaskInteractor
import com.ivanasharov.smartplanner.presentation.converters.ConvertDomainToUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ShowTaskViewModel @ViewModelInject constructor(
    private val mAddTaskInteractor: AddTaskInteractor,
    private val mConvertDomainToUI: ConvertDomainToUI
): BaseViewModel() {

    private val mTime = MutableLiveData<String>()
    private val mTaskViewModel  = MutableLiveData<TaskViewModel>()
    private val mIsLoading= MutableLiveData<Boolean>(true)

    val isLoading: LiveData<Boolean>
        get() = mIsLoading
    val task: LiveData<TaskViewModel>
        get() = mTaskViewModel

    val time: LiveData<String>
        get() = mTime

    fun loadTask(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            mAddTaskInteractor.loadTask(id).map {
                TaskViewModel(mConvertDomainToUI.taskDomainToTaskUILoad(it))
            }.collect{
                mTaskViewModel.postValue(it)
                mTime.postValue(getTime(it))
                mIsLoading.postValue( false)
            }
        }
    }

    private fun getTime(taskViewModel: TaskViewModel): String {
        return "${taskViewModel.timeFrom} - ${taskViewModel.timeTo}"
    }

}