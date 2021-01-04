package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.domain.AddTaskInteractor
import com.ivanasharov.smartplanner.presentation.ConvertDomainToUI
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
//    val taskUI = TaskUI()
//    var  gMap : GoogleMap? = null
    var isCorrectAddress = false
    private val mTaskViewModel  = MutableLiveData<TaskViewModel>()
    private val mIsLoading= MutableLiveData<Boolean>(true)
    /*    private val mIsLoadingCalendars = MutableLiveData<Boolean>(false)
        private val mIsLoadingTask = MutableLiveData<Boolean>(false)*/
    private val mNamesCalendarsList = MutableLiveData<List<String>>()
    val isLoading: LiveData<Boolean>
        get() = mIsLoading
    val task: LiveData<TaskViewModel>
        get() = mTaskViewModel

    val time: LiveData<String>
        get() = mTime

    fun loadTask(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            //mIsLoading.postValue(true)
            mAddTaskInteractor.loadTask(id).map {
                TaskViewModel(mConvertDomainToUI.taskDomainToTaskUILoad(it))
            }.collect{
                mTaskViewModel.postValue(it)
                mTime.postValue(getTime(it))
                mIsLoading.postValue( false)
                // taskUI.postValue(it)
            }

            Log.d("run", "cxz")
            //   mIsLoading.postValue(false)
        }
        Log.d("run", "cxz")
    }

    private fun getTime(taskViewModel: TaskViewModel): String {
        return "${taskViewModel.timeFrom} - ${taskViewModel.timeTo}"
    }
/*    private fun loadMap() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun onMapReady() {
        gMap?.setMinZoomPreference(12f)
        gMap?.isIndoorEnabled = true
        val uiSettings = gMap?.uiSettings
        uiSettings?.isIndoorLevelPickerEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isMapToolbarEnabled = true
        uiSettings?.isCompassEnabled = true
        uiSettings?.isZoomControlsEnabled = true

        if (taskUI.address.value != null) {
            val coordinates = getCoordinaty()
            if (coordinates != null) {
                isCorrectAddress = true
                gMap?.addMarker(MarkerOptions().position(coordinates))
                gMap?.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }
    */


/*
    val taskUI = TaskUI()
    var  gMap : GoogleMap? = null
    var isCorrectAddress = false

    init {
        taskUI.isAddToCalendar.value =false
    }

   private fun longToCalendar(timeInMillis : Long?) : GregorianCalendar?{
        if (timeInMillis != null) {
            val calendar: Calendar = Calendar.getInstance()
           calendar.timeInMillis = timeInMillis
            return GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)
            )
        }
        else return null
    }

    fun init(intent: Intent) {
        taskUI.name.value = intent.getStringExtra("name")
        taskUI.description.value = intent.getStringExtra("description")
        taskUI.date.value = longToCalendar(intent.getLongExtra("date", 0))
        taskUI.timeFrom.value = intent.getStringExtra("timeFrom")
        taskUI.timeTo.value = intent.getStringExtra("timeTo")
        taskUI.importance.value = intent.getStringExtra("importance")
        taskUI.address.value = intent.getStringExtra("address")
        taskUI.isSnapContact.value = intent.getBooleanExtra("isSnapContact", false)
        taskUI.contact.value = intent.getStringExtra("contact")
        taskUI.status.value = intent.getBooleanExtra("status", false)
    }

    private fun loadMap() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun onMapReady() {
        gMap?.setMinZoomPreference(12f)
        gMap?.isIndoorEnabled = true
        val uiSettings = gMap?.uiSettings
        uiSettings?.isIndoorLevelPickerEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isMapToolbarEnabled = true
        uiSettings?.isCompassEnabled = true
        uiSettings?.isZoomControlsEnabled = true

        if (taskUI.address.value != null) {
            val coordinates = getCoordinaty()
            if (coordinates != null) {
                isCorrectAddress = true
                gMap?.addMarker(MarkerOptions().position(coordinates))
                gMap?.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }

*/
}