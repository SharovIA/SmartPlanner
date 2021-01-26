package com.ivanasharov.smartplanner.shared.model

import com.ivanasharov.smartplanner.utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.data.model.WeatherData

data class WeatherWithStatus (
    var weatherData: WeatherData?,
    val weatherStatusCode: WeatherStatusCode,
    var isDefault: Boolean
)