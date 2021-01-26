package com.ivanasharov.smartplanner.data.repositories.weather

import com.ivanasharov.smartplanner.shared.model.WeatherWithStatus
import com.ivanasharov.smartplanner.data.model.Coordinates
import kotlinx.coroutines.flow.Flow

interface RemoteWeatherRepository {

    fun loadWeather(coordinates: Coordinates): Flow<WeatherWithStatus>

    fun getCoordinaties(): Coordinates

}