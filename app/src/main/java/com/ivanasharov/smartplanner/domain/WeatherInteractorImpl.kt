package com.ivanasharov.smartplanner.domain

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

/*    override fun loadWeather(permission: Boolean): Flow<WeatherData> = mWeatherRepository.loadWeather().map {
        executeWeather(it)
    }*/

    override fun loadWeather(permission: Boolean): Flow<WeatherData> {
/*        if (permission){
            mWeatherRepository.getCoordinaties().map{
                if (it.latitude != null && it.longitude != null){
                    mWeatherRepository.loadWeather(it)
                } else
                    mWeatherRepository.loadWeather(mDefaultCoordinates)
            }
        } else{
            return mWeatherRepository.loadWeather(mDefaultCoordinates)
        }*/
        if (permission){
            val coordinates = mWeatherRepository.getCoordinaties()
            if (coordinates.latitude != null && coordinates.longitude != null){
                mDefaultCoordinates = coordinates
            }
        }
        return mWeatherRepository.loadWeather(mDefaultCoordinates).map {
            executeWeather(it)
        }
    }

    private fun executeWeather(weather: WeatherData): WeatherData {
        return WeatherData(weather.namePlace, weather.description, weather.icon, weather.temp - ABS_MULL,
        weather.tempFeels - ABS_MULL, (weather.pressure* G_PA).toInt(), weather.humidity, weather.speedWind, weather.degWind, weather.isNight)
    }
}