package com.google.andreikesel.weather.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.andreikesel.R
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

    fun searchCity(nameCity: String, context: Context) {
        viewModelScope.launch {
            try {
                val list: List<WeatherResult> = apiRepository.getApiResultCity(nameCity)
                if (list.isNotEmpty()) {
                    liveData.postValue(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, R.string.search_is_empty, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addWeatherCity(weatherResult: WeatherResult) {
        viewModelScope.launch {
            apiRepository.addWeatherCityToDatabase(
                weatherResult.lat,
                weatherResult.lon,
                weatherResult.description,
                weatherResult.name,
                weatherResult.temp,
                weatherResult.humidity,
                weatherResult.feelsLike,
                weatherResult.iconId,
                weatherResult.windSpeed
            )
        }
    }
}