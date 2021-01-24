package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherInteractor {

    fun loadWeather(permission: Boolean): Flow<WeatherData>
}