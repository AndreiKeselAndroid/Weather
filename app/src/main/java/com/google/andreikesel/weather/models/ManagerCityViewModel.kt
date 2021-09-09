package com.google.andreikesel.weather.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class ManagerCityViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), KoinComponent {

    val liveData: LiveData<List<WeatherResult>> =
        apiRepository.getWeatherCityOutDatabase().map {
            it.map { weatherResult ->
                WeatherResult(
                    weatherResult.description,
                    weatherResult.name,
                    weatherResult.temp,
                    weatherResult.humidity,
                    weatherResult.feelsLike,
                    weatherResult.iconId,
                    weatherResult.windSpeed
                )
            }

        }.asLiveData()

    fun deleteCity(weatherResult: WeatherResult) {

        viewModelScope.launch {
            apiRepository.deleteWeatherCityOutDatabase(weatherResult)
        }
    }
}