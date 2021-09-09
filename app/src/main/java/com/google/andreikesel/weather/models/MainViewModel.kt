package com.google.andreikesel.weather.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentMainBinding
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.repository.ApiRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class MainViewModel(
    private val apiRepository: ApiRepository
) : ViewModel(), KoinComponent {

    private val _mutableLiveData = MutableLiveData<WeatherResult>()
    val livedata: LiveData<WeatherResult> = _mutableLiveData

    fun update (lat: Double, lon: Double,bindingView:FragmentMainBinding) {

        viewModelScope.launch {
            val name = apiRepository.getApiResultCoordinates(lat, lon)
            _mutableLiveData.postValue(name)

            val url = "https://openweathermap.org/img/wn/${name.iconId}@2x.png"

            Glide
                .with(bindingView.root)
                .load(url)
                .placeholder(R.drawable.ic_cloudy)
                .into(bindingView.weatherImage)
        }
    }
}