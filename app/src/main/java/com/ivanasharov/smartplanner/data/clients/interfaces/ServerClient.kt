package com.ivanasharov.smartplanner.data.clients.interfaces

import com.ivanasharov.smartplanner.data.clients.dto.ServerWeather

interface ServerClient {
    suspend fun getCurrentWeather(lat: Double, lon: Double, language: String):ServerWeather?
}