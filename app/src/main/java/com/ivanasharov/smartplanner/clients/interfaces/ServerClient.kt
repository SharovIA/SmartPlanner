package com.ivanasharov.smartplanner.clients.interfaces

import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import kotlinx.coroutines.flow.Flow


interface ServerClient {

    suspend fun getCurrentWeather(lat: Double, lon: Double, language: String):ServerWeather
}