package com.ivanasharov.smartplanner

import com.ivanasharov.smartplanner.Utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.data.model.WeatherData

data class WeatherWithStatus (
    var weatherData: WeatherData?,
    val weatherStatusCode: WeatherStatusCode,
    var isDefault: Boolean
)