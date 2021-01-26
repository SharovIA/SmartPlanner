package com.ivanasharov.smartplanner.data.repositories

import com.ivanasharov.smartplanner.WeatherWithStatus
import com.ivanasharov.smartplanner.data.model.Coordinates
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import kotlinx.coroutines.flow.Flow

interface RemoteWeatherRepository {

   // suspend fun getWeatherData(): WeatherData
    fun loadWeather(coordinates: Coordinates): Flow<WeatherWithStatus>
    fun getCoordinaties(): Coordinates
    //fun getCoordinaties(): Flow<Coordinates>
  //  suspend fun getInfoAboutWeather(): ServerWeather
}