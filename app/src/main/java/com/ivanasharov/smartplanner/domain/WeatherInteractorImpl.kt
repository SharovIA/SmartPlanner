package com.ivanasharov.smartplanner.domain

import com.ivanasharov.smartplanner.WeatherWithStatus
import com.ivanasharov.smartplanner.data.model.Coordinates
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.data.repositories.RemoteWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherInteractorImpl @Inject constructor(
    private val mWeatherRepository: RemoteWeatherRepository
): WeatherInteractor {

    companion object{
        private const val ABS_MULL = 273
        private const val G_PA = 0.75
    }

    private var mDefaultCoordinates = Coordinates(55.7522, 37.6156)
    private var mIsDefaultCoordinates = true

/*    override fun loadWeather(permission: Boolean): Flow<WeatherData> = mWeatherRepository.loadWeather().map {
        executeWeather(it)
    }*/

/*    override fun loadWeather(permission: Boolean): Flow<WeatherData> {
        if (permission){
            val coordinates = mWeatherRepository.getCoordinaties()
            if (coordinates.latitude != null && coordinates.longitude != null){
                mDefaultCoordinates = coordinates
            }
        }
        return mWeatherRepository.loadWeather(mDefaultCoordinates).map {
            executeWeather(it)
        }
    }*/
override fun loadWeather(permission: Boolean): Flow<WeatherWithStatus> {
    mIsDefaultCoordinates = true
    if (permission){
        val coordinates = mWeatherRepository.getCoordinaties()
        if (coordinates.latitude != null && coordinates.longitude != null){
            mDefaultCoordinates = coordinates
            mIsDefaultCoordinates = false
        }
    }
    return mWeatherRepository.loadWeather(mDefaultCoordinates).map {
        if(it.weatherData != null){
            executeWeather(it)
        }
        else it
    }
}

    private fun executeWeather(weatherWithStatus: WeatherWithStatus): WeatherWithStatus {
        val weather = weatherWithStatus.weatherData as WeatherData
        val weatherData = WeatherData(weather.namePlace, weather.description, weather.icon, weather.temp - ABS_MULL,
        weather.tempFeels - ABS_MULL, (weather.pressure* G_PA).toInt(), weather.humidity, weather.speedWind, weather.degWind, weather.isNight)
        weatherWithStatus.weatherData = weatherData
        weatherWithStatus.isDefault = mIsDefaultCoordinates
        return weatherWithStatus
    }
}