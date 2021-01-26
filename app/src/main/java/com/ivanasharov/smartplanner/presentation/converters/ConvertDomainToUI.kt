package com.ivanasharov.smartplanner.presentation.converters

import android.annotation.SuppressLint
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.domain.model.TaskDomain
import com.ivanasharov.smartplanner.presentation.model.TaskUI
import com.ivanasharov.smartplanner.presentation.model.TaskUILoad
import com.ivanasharov.smartplanner.presentation.model.WeatherDataViewModel
import java.util.*
import javax.inject.Inject

class ConvertDomainToUI @Inject constructor(
    private val mResources: ResourceProvider
) {

fun weatherDataToWeatherDataViewModel(weather: WeatherData): WeatherDataViewModel{
    return WeatherDataViewModel(weather.namePlace, weather.description, weather.icon, getTemp(weather.temp), getFeelsLike(weather.tempFeels),
        getPressure(weather.pressure), getHumidity(weather.humidity), getWind(weather.speedWind, weather.degWind),
        getBackGround(weather.isNight), isNight(weather.isNight))
}

    private fun isNight(night: Boolean?): Boolean {
        return night != null && night
    }

    private fun getFeelsLike(temp: Int): String = mResources.string(R.string.feels_like) +" " + getTemp(temp)

    @SuppressLint("ResourceType")
    private fun getBackGround(isNight: Boolean?): Int {
        if (isNight!=null){
            if (isNight) return mResources.color(R.color.background_night)
            else return mResources.color(R.color.background_day)
        }
        return mResources.color(R.color.background_nan)
    }

    private fun getWind(speedWind: Double, degWind: Int): String {
        if(degWind == 0) return speedWind.toString() +" "+ mResources.string(R.string.wind_ms_north)
        if(degWind == 90) return speedWind.toString() +" "+ mResources.string(R.string.wind_ms_east)
        if(degWind == 180) return speedWind.toString() + " "+mResources.string(R.string.wind_ms_south)
        if(degWind == 270) return speedWind.toString() +" "+ mResources.string(R.string.wind_ms_west)

        if(degWind in 1..89) return speedWind.toString() + " "+mResources.string(R.string.wind_ms_north_east)
        if(degWind in 91..179) return speedWind.toString() +" "+ mResources.string(R.string.wind_ms_southeast)
        if(degWind in 181..269) return speedWind.toString() + " "+mResources.string(R.string.wind_ms_southwest)
        return speedWind.toString() +" "+ mResources.string(R.string.wind_ms_northwest)
    }

    private fun getHumidity(humidity: Int): String  = "$humidity %"

    private fun getPressure(pressure: Int): String = pressure.toString() +" "+ mResources.string(R.string.ed_pressure)

    private fun getTemp(temp: Int): String ="$temp c̊"


    fun taskDomainToTaskUILoad(taskDomain: TaskDomain): TaskUILoad {
    return TaskUILoad(taskDomain.name as String, taskDomain.description,
    convertDate(taskDomain.date), convertTime(taskDomain.timeFrom),
    convertTime( taskDomain.timeTo), ConvertImportance(taskDomain.importance),
    taskDomain.address, taskDomain.contact,  taskDomain.status, taskDomain.id)
}

    fun taskDomainToTaskUI(taskDomain: TaskDomain): TaskUI {
        val taskUI = TaskUI()
        taskUI.name.postValue(taskDomain.name)
        taskUI.description.postValue( taskDomain.description)
        taskUI.date.postValue(convertDate(taskDomain.date))
        taskUI.timeFrom.postValue(convertTime(taskDomain.timeFrom))
        taskUI.timeTo.postValue(convertTime( taskDomain.timeTo))
        taskUI.importance.postValue(ConvertImportance(taskDomain.importance))
        taskUI.address.postValue( taskDomain.address)
        taskUI.contact.postValue( taskDomain.contact)
        taskUI.status.postValue( taskDomain.status)
        return taskUI
    }

    private fun ConvertImportance(importance: Int): String? {
        val categories = getArray()
        when(importance){
            1 -> return categories[4]
            2-> return categories[3]
            3 -> return categories[2]
            4 -> return categories[1]
            else -> return ""
        }
    }

    private fun getArray(): Array<String> = mResources.array(R.array.importance)

    private fun convertTime(calendar: GregorianCalendar?): String {
        val hours = calendar?.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar?.get(Calendar.MINUTE)
        return "${getTextValue(hours)}:${getTextValue(minutes)}"
    }

    private fun convertDate(calendar: GregorianCalendar?): String {
        val year = calendar?.get(Calendar.YEAR)
        val month = calendar?.get(Calendar.MONTH)
        val dayOfMonth = calendar?.get(Calendar.DAY_OF_MONTH)
        return "${getTextValue(dayOfMonth)}-${getTextValue(month as Int  +1)}-${getTextValue(year)}"
    }

    private fun getTextValue(number: Int?): String? {
        if (number!=null){
            when (number < 10) {
                true -> return "0$number"
                false -> return "$number"
            }
        }
        else  return null
    }
}
