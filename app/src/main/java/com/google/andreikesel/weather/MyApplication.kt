package com.google.andreikesel.weather

import android.app.Application
import com.google.andreikesel.weather.cloud.WeatherApi
import com.google.andreikesel.weather.database.DatabaseConstructor
import com.google.andreikesel.weather.database.WeatherCityDatabase
import com.google.andreikesel.weather.models.MainViewModel
import com.google.andreikesel.weather.models.ManagerCityViewModel
import com.google.andreikesel.weather.models.SearchViewModel
import com.google.andreikesel.weather.repository.ApiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KoinApiExtension
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(viewModel, api, repository, storageModule))
        }
    }

    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }
        factory { get<WeatherCityDatabase>().myWeatherCityDao() }
    }

    private val api = module {
        single { WeatherApi.get() }
    }

    private val repository = module {
        factory { ApiRepository(get(), get()) }
    }

    private val viewModel = module {
        viewModel { MainViewModel(get()) }
        viewModel { ManagerCityViewModel(get()) }
        viewModel { SearchViewModel(get()) }
    }
}