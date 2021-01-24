package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.data.server_dto.ServerWeather

class ConvertWeatherDTOtoWeatherData {

    companion object{
        private const val START_IMG_URL = "http://openweathermap.org/img/wn/"
        private const val END_IMG_URL = "@2x.png"
    }

    fun convert(serverWeather: ServerWeather): WeatherData {
        val weather = serverWeather.weather[0]
        val main = serverWeather.main
        val wind = serverWeather.wind
        return WeatherData(serverWeather.name, weather.description, getUriIcon(weather.icon),
        main.temp.toInt(), main.feels_like.toInt(), main.pressure, main.humidity, wind.speed, wind.deg, getStatusDay(weather.icon))
    }

    private fun getStatusDay(icon: String): Boolean? {
        val length = icon.length
        if (length > 0) {
            val ch = icon[length - 1]
            when (ch){
                'd' -> return false
                'n' -> return true
            }
        }

        return null
    }

    private fun getUriIcon(name: String): String = START_IMG_URL + name + END_IMG_URL
}