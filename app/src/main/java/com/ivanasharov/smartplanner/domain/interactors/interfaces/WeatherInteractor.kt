package com.ivanasharov.smartplanner.domain.interactors.interfaces

import com.ivanasharov.smartplanner.shared.model.WeatherWithStatus
import kotlinx.coroutines.flow.Flow

interface WeatherInteractor {

    fun loadWeather(permission: Boolean): Flow<WeatherWithStatus>

}