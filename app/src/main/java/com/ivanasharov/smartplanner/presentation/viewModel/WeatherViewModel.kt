package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
import com.ivanasharov.smartplanner.Utils.statuscode.WeatherStatusCode
import com.ivanasharov.smartplanner.data.model.WeatherData
import com.ivanasharov.smartplanner.data.repositories.RemoteWeatherRepository
import com.ivanasharov.smartplanner.data.server_dto.ServerWeather
import com.ivanasharov.smartplanner.domain.CurrentTasksInteractor
import com.ivanasharov.smartplanner.domain.WeatherInteractor
import com.ivanasharov.smartplanner.presentation.ConvertDomainToUI
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.model.WeatherDataViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WeatherViewModel@ViewModelInject constructor(
    private val resources: ResourceProvider,
    private val mWeatherInteractor: WeatherInteractor,
    private val mConvertDomainToUI: ConvertDomainToUI
) : BaseViewModel() {

    //чтобы никто не мог изменить
    private val mWeather = MutableLiveData<WeatherDataViewModel>()
    private val mIsLoading = MutableLiveData<Boolean>(true)
    private val mIsErrorLoading = MutableLiveData<Boolean>(true)
    private val mStatus = MutableLiveData<String>("")
    private val mIsDefaultCity = MutableLiveData<Boolean>(false)
    //чтобы можно было получить данные
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
        Log.d("WE", "NNOO")
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
            Log.d("test", "gfdsa")
            mIsLoading.postValue(false)
        }
        Log.d("test", "gfdsa")
    }

    private fun getMessage(code: WeatherStatusCode): String {
        when(code){
            WeatherStatusCode.SERVER_RESPONSE_IS_EMPTY_OR_CONNECTION_BROKEN -> return resources.string(
                            R.string.server_is_temporarily_unavailable)
            else -> return resources.string(R.string.no_internet_connection)
        }
    }


}
