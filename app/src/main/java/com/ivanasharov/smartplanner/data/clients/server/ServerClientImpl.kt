package com.ivanasharov.smartplanner.data.clients.server

import com.google.gson.GsonBuilder
import com.ivanasharov.smartplanner.data.clients.interfaces.ApiDefinition
import com.ivanasharov.smartplanner.data.clients.interfaces.ServerClient
import com.ivanasharov.smartplanner.data.clients.dto.ServerWeather
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ServerClientImpl @Inject constructor() : ServerClient {

    private val mService: ApiDefinition

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/"
    }

    init {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
        mService = retrofit.create(ApiDefinition::class.java)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        language: String
    ): ServerWeather? {
        try {
            return mService.loadDataOfWeather(lat, lon, language).await()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}