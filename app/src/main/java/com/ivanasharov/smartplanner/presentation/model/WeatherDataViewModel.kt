package com.ivanasharov.smartplanner.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.ivanasharov.smartplanner.data.model.WeatherData

/*
data class WeatherDataViewModel(
    private val weatherData: WeatherData,
    val namePlace : String = weatherData.namePlace,
    val description : String = weatherData.description,
    val icon : String = weatherData.icon,
    val temp: String = weatherData.temp.toString(),
    val tempFeels: String = weatherData.tempFeels.toString(),
    val pressure : String = weatherData.pressure.toString(),
    val humidity: String = weatherData.humidity.toString(),
    val speedWind: String = weatherData.speedWind.toString(),
    val degWind: String = weatherData.degWind.toString()
)*/
data class WeatherDataViewModel(
    val namePlace : String,
    val description : String,
    val icon : String,
    val temp: String,
    val tempFeels: String,
    val pressure : String,
    val humidity: String,
    val wind: String,
    val background : Int,
    val isNight:Boolean
)