package com.ivanasharov.smartplanner.presentation.viewModel

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.DailyScheduleInteractor
import com.ivanasharov.smartplanner.domain.NameDurationAndImportanceTask
import com.ivanasharov.smartplanner.presentation.model.Hour
import com.ivanasharov.smartplanner.presentation.model.Minute
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DailyScheduleViewModel  @ViewModelInject constructor(
    private val resources: ResourceProvider,
    private val mDailyScheduleInteractor: DailyScheduleInteractor,
    private val mCurrentTasksInteractor: CurrentTasksInteractor
) : BaseViewModel() {

    private val mListTask = MutableLiveData<List<Hour>>()

    private lateinit var mListHoursWithTasks: MutableList<Hour>
    private val mDate  = MutableLiveData<String>()
    private val mIsLoading = MutableLiveData<Boolean>(true)

    val listTasks :LiveData<List<Hour>>
        get() = mListTask
    val date : LiveData<String>
        get() = mDate
    val isLoading: LiveData<Boolean>
        get() = mIsLoading


    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch(Dispatchers.IO){
            mIsLoading.postValue(true)
            mDate.postValue(getDate())
            getListDefault()
            mDailyScheduleInteractor.getDailySchedule().collect { list ->
                list.map {
                    chooseCase(it)
                    }
                mListTask.postValue(mListHoursWithTasks)
                mIsLoading.postValue(false)
            }
        }
    }

    private fun getListDefault() {
        mListHoursWithTasks =  mutableListOf(
            Hour("00:00", Color.WHITE, null), Hour("01:00", Color.WHITE, null),
            Hour("02:00", Color.WHITE, null), Hour("03:00", Color.WHITE, null),
            Hour("04:00", Color.WHITE, null), Hour("05:00", Color.WHITE, null),
            Hour("06:00", Color.WHITE, null), Hour("07:00", Color.WHITE, null),
            Hour("08:00", Color.WHITE, null), Hour("09:00", Color.WHITE, null),
            Hour("10:00", Color.WHITE, null), Hour("11:00", Color.WHITE, null),
            Hour("12:00", Color.WHITE, null), Hour("13:00", Color.WHITE, null),
            Hour("14:00", Color.WHITE, null), Hour("15:00", Color.WHITE, null),
            Hour("16:00", Color.WHITE, null), Hour("17:00", Color.WHITE, null),
            Hour("18:00", Color.WHITE, null), Hour("19:00", Color.WHITE, null),
            Hour("20:00", Color.WHITE, null), Hour("21:00", Color.WHITE, null),
            Hour("22:00", Color.WHITE, null), Hour("23:00", Color.WHITE, null))

    }

    private fun chooseCase(task: NameDurationAndImportanceTask) {
        val color: Int = getColorImportance(task.importance)
        if(task.inOneHour && task.startMinute != null && task.finishMinuteInStartHour != null ) {
            //внутри одного часа
            changeMinutes(task.startHour, task.startMinute, task.finishMinuteInStartHour, color)
        }
        else if((!task.isFullHours) && task.startMinute != null && task.finishMinute != null) {
            //начало в однoм часе, конец в след.часе (действие в 2х часах)_
            changeMinutes(hour = task.startHour, startMinute =  task.startMinute, color =  color)
            changeMinutes(hour = task.startHour+1, finishMinute =  task.finishMinute, color =  color)
        }
        else if(task.isFullHours && task.finishMinute != null && task.finishHour != null && task.startMinute != null && task.countHours != null) {
            //начало в однoм часе, неск.полных часов и конец в след.часе (действие более 2х часов)_
            changeMinutes(hour = task.startHour, startMinute = task.startMinute, color = color)
            changeHours(task.startHour+1, task.countHours, color)
            changeMinutes(hour = task.finishHour, finishMinute =  task.finishMinute, color =  color)
        } else if(task.isFullHours && (!task.isNotFullStartHour) && task.isNotFullFinishHour && task.countHours != null &&task.finishHour != null && task.finishMinute != null){
            //неск полных часов и неск.минут
            changeHours(task.startHour, task.countHours, color)
            changeMinutes(hour = task.finishHour, finishMinute =  task.finishMinute, color =  color)
        } else if (task.countHours != null){
            //неск.полных часов
            changeHours(task.startHour, task.countHours, color)
        }
    }

    private fun changeHours(startHour: Int, countHours: Int, color: Int ) {
        var index = 0
        while (index < countHours) {
            mListHoursWithTasks[startHour+index].color = color
            index += 1
        }
    }

    private fun changeMinutes(hour: Int, startMinute: Int = 0, finishMinute: Int = 59, color: Int ) {
        var minutes = mListHoursWithTasks[hour].colorMinutes
        if (minutes == null)
            minutes = MutableList(60){
           Color.WHITE
        }

        for (index in startMinute..finishMinute)
            minutes[index] = color

        mListHoursWithTasks[hour].colorMinutes =minutes
    }

    @SuppressLint("ResourceType")
    private fun getColorImportance(importance: Int?): Int {
        when (importance){
            4 -> return resources.color(R.color.important_and_urgent)
            3-> return resources.color(R.color.not_important_and_urgent)
            2 -> return resources.color(R.color.important_and_not_urgent)
            1 -> return resources.color(R.color.not_important_and_not_urgent)
            else -> return resources.color(R.color.no_importance)
        }
    }

    private fun getDate(): String {
        val day = mCurrentTasksInteractor.getCurrentDayOfWeek()
        val nameOfDay = getNameDay(day)
        val currentDate = mCurrentTasksInteractor.getCurrentDate()

        return "$nameOfDay, $currentDate"
    }

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
}