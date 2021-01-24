package com.ivanasharov.smartplanner.presentation.viewModel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.Utils.ResourceProvider
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
    private val mWeatherInteractor: WeatherInteractor,
    private val mConvertDomainToUI: ConvertDomainToUI
) : BaseViewModel() {

    //чтобы никто не мог изменить
    private val mWeather = MutableLiveData<WeatherDataViewModel>()
    private val mIsLoading = MutableLiveData<Boolean>(true)
    //чтобы можно было получить данные
    val weather: LiveData<WeatherDataViewModel>
        get() = mWeather

    val isLoading: LiveData<Boolean>
        get() = mIsLoading

  //  init {
  //      getData()
  //  }

/*    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mWeather.postValue(WeatherDataViewModel(mRemoteWeatherRepository.getWeatherData()))
            Log.d("test", "gfdsa")
            mIsLoading.postValue(false)
        }
    }*/

    fun getData(permission: Boolean) {
        Log.d("WE", "NNOO")
        viewModelScope.launch(Dispatchers.IO) {
            mIsLoading.postValue(true)
            mWeatherInteractor.loadWeather(permission).map {
                mConvertDomainToUI.weatherDataToWeatherDataViewModel(it)
            }.collect{
                mWeather.postValue(it)
            }
            Log.d("test", "gfdsa")
            mIsLoading.postValue(false)
        }
        Log.d("test", "gfdsa")
    }


}
