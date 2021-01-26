package com.ivanasharov.smartplanner.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ivanasharov.smartplanner.Utils.NetworkManager
import com.ivanasharov.smartplanner.Utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.WeatherWithStatus
import com.ivanasharov.smartplanner.clients.interfaces.ServerClient
import com.ivanasharov.smartplanner.data.ConvertWeatherDTOtoWeatherData
import com.ivanasharov.smartplanner.data.model.Coordinates
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class RemoteWeatherRepositoryImpl @Inject constructor(
    private val mServerClient: ServerClient,
    private val mLocationManager: LocationManager,
    private val mContext: Context
) : RemoteWeatherRepository {


    private var hasGPS = false
    private var hasNetwork = false
    private var mLocationGPS: Location? = null
    private var mLocationNetwork: Location? = null
    private lateinit var mCoordinates: Coordinates


    /*   override suspend fun getWeatherData(): WeatherData {
           val language = Locale.getDefault().language
           val serverWeather = mServerClient.getCurrentWeather(0.0, 0.0, language)
           return ConvertWeatherDTOtoWeatherData().convert(serverWeather)
       }*/

/*    override fun loadWeather(coordinates: Coordinates): Flow<WeatherData> = flow {
        val language = Locale.getDefault().language
        val serverWeather = mServerClient.getCurrentWeather(
            coordinates.latitude as Double,
            coordinates.longitude as Double,
            language
        )
        emit(ConvertWeatherDTOtoWeatherData().convert(serverWeather))
    }*/

    override fun loadWeather(coordinates: Coordinates): Flow<WeatherWithStatus> = flow {
        val language = Locale.getDefault().language
        if (NetworkManager().isNetworkAvailable(mContext)) {
            val serverWeather = mServerClient.getCurrentWeather(coordinates.latitude as Double,
                coordinates.longitude as Double, language)

            if(serverWeather == null){
                emit(WeatherWithStatus(null, WeatherStatusCode.SERVER_RESPONSE_IS_EMPTY_OR_CONNECTION_BROKEN, false))
            } else {
                val weatherData = ConvertWeatherDTOtoWeatherData().convert(serverWeather)
                emit(WeatherWithStatus(weatherData, WeatherStatusCode.SUCCESS, false))
            }
        } else{
            val weatherWithStatus = WeatherWithStatus(null, WeatherStatusCode.NO_INTERNET_CONNECTION, false)
            emit(weatherWithStatus)
        }
    }


    override fun getCoordinaties(): Coordinates {
        getLocation()
        return mCoordinates
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d("WE", "ok")
        hasNetwork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        hasGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGPS || hasNetwork) {
            if (hasGPS) {
                Log.d("WE", "hasGPS")
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            mLocationGPS = location
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                        }

                        override fun onProviderEnabled(provider: String?) {}
                        override fun onProviderDisabled(provider: String?) {}
                    },
                    Looper.getMainLooper()
                )
                val localGPSLocation =
                    mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGPSLocation != null)
                    mLocationGPS = localGPSLocation
            }

                if (hasNetwork) {
                    Log.d("WE", "hasNetwork")
                    mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0F,
                        object : LocationListener {
                            override fun onLocationChanged(location: Location?) {
                                mLocationNetwork = location
                            }

                            override fun onStatusChanged(
                                provider: String?,
                                status: Int,
                                extras: Bundle?
                            ) {
                            }

                            override fun onProviderEnabled(provider: String?) {}
                            override fun onProviderDisabled(provider: String?) {}
                        })
                    val localNetworkLocation =
                        mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (localNetworkLocation != null)
                        mLocationNetwork = localNetworkLocation
                }

                if (mLocationGPS != null && mLocationNetwork != null) {
                    val radiusGPS = mLocationGPS?.accuracy
                    val radiusNetwork = mLocationGPS?.accuracy
                    if (radiusGPS != null && radiusNetwork != null && radiusGPS > radiusNetwork) {
                        Log.d(
                            "WE",
                            "network lat: ${mLocationNetwork?.latitude} and lon: ${mLocationNetwork?.longitude}"
                        )
                        mCoordinates =
                            Coordinates(mLocationNetwork?.latitude, mLocationNetwork?.longitude)
                    } else {
                        Log.d(
                            "WE",
                            "gps lat: ${mLocationGPS?.latitude} and lon: ${mLocationGPS?.longitude}"
                        )
                        mCoordinates = Coordinates(mLocationGPS?.latitude, mLocationGPS?.longitude)
                    }
                } else if (mLocationGPS != null) {
                    Log.d(
                        "WE",
                        "network lat: ${mLocationGPS?.latitude} and lon: ${mLocationGPS?.longitude}"
                    )
                    mCoordinates = Coordinates(mLocationGPS?.latitude, mLocationGPS?.longitude)
                } else if (mLocationNetwork != null) {
                    Log.d(
                        "WE",
                        "network lat: ${mLocationNetwork?.latitude} and lon: ${mLocationNetwork?.longitude}"
                    )
                    mCoordinates =
                        Coordinates(mLocationNetwork?.latitude, mLocationNetwork?.longitude)
                } else {
                    Log.d("WE", "NO")
                    mCoordinates = Coordinates(null, null)
                }

            } else {
                Log.d("WE", "NO")
                mCoordinates = Coordinates(null, null)
            }
        }

}