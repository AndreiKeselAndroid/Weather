package com.google.andreikesel.weather.models

import androidx.lifecycle.ViewModel
import com.google.andreikesel.weather.repository.ApiRepository
import org.koin.core.component.KoinComponent

class MainViewModel (
    private val apiRepository: ApiRepository
): ViewModel(), KoinComponent {
}