package com.google.andreikesel.weather

import android.app.Application
import com.google.andreikesel.weather.cloud.WeatherApi
import com.google.andreikesel.weather.database.DatabaseConstructor
import com.google.andreikesel.weather.database.WeatherCityDatabase
import com.google.andreikesel.weather.repository.ApiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(listOf(api, repository, storageModule))
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
        factory { ApiRepository(get(),get()) }
    }
}