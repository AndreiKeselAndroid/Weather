package com.google.andreikesel.weather.models

import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.andreikesel.databinding.FragmentMainBinding
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.repository.ApiRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class MainViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), KoinComponent {

    private val _mutableFlow = MutableStateFlow<WeatherResult?>(null)
    val stateFlow: StateFlow<WeatherResult?> = _mutableFlow

    fun update(lat: Double, lon: Double, bindingView: FragmentMainBinding) {

        viewModelScope.launch {

            try {
                apiRepository.addSavedCityWeatherToDatabase(lat, lon)
                apiRepository.getSavedCityWeatherOutDatabase()
                    .collect {
                        _mutableFlow.value = it
                        val iconId = it.iconId

                        val url =
                            "https://openweathermap.org/img/wn/${iconId}@2x.png"

                        Glide
                            .with(bindingView.root)
                            .load(url)
                            .into(bindingView.weatherImage)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateIfLocationNull(bindingView: FragmentMainBinding) {

        viewModelScope.launch {

            try {
                apiRepository.getSavedCityWeatherOutDatabase()
                    .collect {
                        _mutableFlow.value = it
                        val iconId = it.iconId

                        val url =
                            "https://openweathermap.org/img/wn/${iconId}@2x.png"

                        Glide
                            .with(bindingView.root)
                            .load(url)
                            .into(bindingView.weatherImage)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}