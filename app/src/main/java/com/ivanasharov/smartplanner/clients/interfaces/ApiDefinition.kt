package com.ivanasharov.smartplanner.clients.interfaces

import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiDefinition {

    companion object{
       private const val  KEY = "38bf06e3bc65bfe91afe1e27efeee8a7"
    }

    @GET("data/2.5/weather")
    fun loadDataOfWeather(@Query("lat") lat: Double,
                          @Query("lon") lon: Double,
                          @Query("lang") lang: String,
                          @Query("appid") key: String = KEY
    ): Deferred<ServerWeather>
}