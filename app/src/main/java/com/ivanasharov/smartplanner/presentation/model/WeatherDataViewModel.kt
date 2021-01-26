package com.ivanasharov.smartplanner.presentation.model

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