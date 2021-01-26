package com.ivanasharov.smartplanner.presentation.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.utils.resources.ResourceProvider
import com.ivanasharov.smartplanner.utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.domain.interactors.interfaces.WeatherInteractor
import com.ivanasharov.smartplanner.presentation.converters.ConvertDomainToUI
import com.ivanasharov.smartplanner.presentation.model.WeatherDataViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherViewModel@ViewModelInject constructor(
    private val mResources: ResourceProvider,
    private val mWeatherInteractor: WeatherInteractor,
    private val mConvertDomainToUI: ConvertDomainToUI
) : BaseViewModel() {

    private val mWeather = MutableLiveData<WeatherDataViewModel>()
    private val mIsLoading = MutableLiveData<Boolean>(true)
    private val mIsErrorLoading = MutableLiveData<Boolean>(true)
    private val mStatus = MutableLiveData<String>("")
    private val mIsDefaultCity = MutableLiveData<Boolean>(false)

    val weather: LiveData<WeatherDataViewModel>
        get() = mWeather

    val isLoading: LiveData<Boolean>
        get() = mIsLoading

    val isErrorLoading: LiveData<Boolean>
        get() = mIsErrorLoading

    val statusError: LiveData<String>
        get() = mStatus

    val isDefaultCity: LiveData<Boolean>
        get() = mIsDefaultCity

    fun getData(permission: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mWeatherInteractor.loadWeather(permission).map {
                if(it.weatherStatusCode == WeatherStatusCode.SUCCESS) {
                    mIsDefaultCity.postValue(it.isDefault)
                    mConvertDomainToUI.weatherDataToWeatherDataViewModel(it.weatherData as WeatherData)

                }
                else {
                    mStatus.postValue(getMessage(it.weatherStatusCode))
                    mIsErrorLoading.postValue(true)
                    null
                }
            }.collect{
                if (it!=null){
                    mWeather.postValue(it)
                    mIsErrorLoading.postValue(false)
                }
            }
            mIsLoading.postValue(false)
        }
    }

    private fun getMessage(code: WeatherStatusCode): String {
        when(code){
            WeatherStatusCode.SERVER_RESPONSE_IS_EMPTY_OR_CONNECTION_BROKEN -> return mResources.string(
                            R.string.server_is_temporarily_unavailable)
            else -> return mResources.string(R.string.no_internet_connection)
        }
    }
}
