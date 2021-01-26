package com.ivanasharov.smartplanner.data.repositories.weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.ivanasharov.smartplanner.utils.network.NetworkManager
import com.ivanasharov.smartplanner.utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.shared.model.WeatherWithStatus
import com.ivanasharov.smartplanner.data.clients.interfaces.ServerClient
import com.ivanasharov.smartplanner.data.converters.ConvertWeatherDTOtoWeatherData
import com.ivanasharov.smartplanner.data.model.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class RemoteWeatherRepositoryImpl @Inject constructor(
    private val mServerClient: ServerClient,
    private val mLocationManager: LocationManager,
    private val mContext: Context
) : RemoteWeatherRepository {

    private var mHasGPS = false
    private var mHasNetwork = false
    private var mLocationGPS: Location? = null
    private var mLocationNetwork: Location? = null
    private lateinit var mCoordinates: Coordinates

    override fun loadWeather(coordinates: Coordinates): Flow<WeatherWithStatus> = flow {
        val language = Locale.getDefault().language
        if (NetworkManager().isNetworkAvailable(mContext)) {
            val serverWeather = mServerClient.getCurrentWeather(coordinates.latitude as Double,
                coordinates.longitude as Double, language)

            if(serverWeather == null){
                emit(
                    WeatherWithStatus(
                        null,
                        WeatherStatusCode.SERVER_RESPONSE_IS_EMPTY_OR_CONNECTION_BROKEN,
                        false
                    )
                )
            } else {
                val weatherData = ConvertWeatherDTOtoWeatherData()
                    .convert(serverWeather)
                emit(
                    WeatherWithStatus(
                        weatherData,
                        WeatherStatusCode.SUCCESS,
                        false
                    )
                )
            }
        } else{
            val weatherWithStatus =
                WeatherWithStatus(
                    null,
                    WeatherStatusCode.NO_INTERNET_CONNECTION,
                    false
                )
            emit(weatherWithStatus)
        }
    }


    override fun getCoordinaties(): Coordinates {
        getLocation()
        return mCoordinates
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mHasNetwork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        mHasGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (mHasGPS || mHasNetwork) {
            if (mHasGPS) {
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

                if (mHasNetwork) {
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
                        mCoordinates =
                            Coordinates(mLocationNetwork?.latitude, mLocationNetwork?.longitude)
                    } else {
                        mCoordinates = Coordinates(mLocationGPS?.latitude, mLocationGPS?.longitude)
                    }

                } else if (mLocationGPS != null) {
                    mCoordinates = Coordinates(mLocationGPS?.latitude, mLocationGPS?.longitude)
                } else if (mLocationNetwork != null) {
                    mCoordinates =
                        Coordinates(mLocationNetwork?.latitude, mLocationNetwork?.longitude)
                } else {
                    mCoordinates = Coordinates(null, null)
                }

        } else {
            mCoordinates = Coordinates(null, null)
        }
    }

}