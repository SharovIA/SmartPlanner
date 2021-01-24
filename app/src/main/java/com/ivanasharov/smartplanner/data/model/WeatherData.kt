package com.ivanasharov.smartplanner.data.model

data class WeatherData(
    val namePlace : String,
    val description : String,
    val icon : String,
    val temp: Int,
    val tempFeels: Int,
    val pressure : Int,
    val humidity: Int,
    val speedWind: Double,
    val degWind: Int,
    val isNight: Boolean?
)