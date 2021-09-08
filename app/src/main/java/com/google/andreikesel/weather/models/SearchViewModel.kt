package com.google.andreikesel.weather.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.repository.ApiRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class SearchViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), KoinComponent {

    val liveData: MutableLiveData<List<WeatherResult>> = MutableLiveData()

    fun searchCity(nameCity: String) {
        viewModelScope.launch {
            val list: List<WeatherResult> = apiRepository.getApiResultCity(nameCity)
            if (list.isNotEmpty()) {
                liveData.postValue(list)
            }
        }
    }
}