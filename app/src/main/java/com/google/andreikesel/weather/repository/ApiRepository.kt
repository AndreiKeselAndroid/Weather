package com.google.andreikesel.weather.repository

import android.text.TextUtils.replace
import com.google.andreikesel.weather.cloud.WeatherApi
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.database.dao.WeatherDao
import com.google.andreikesel.weather.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApiRepository(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) {
    suspend fun getApiResultCity(nameCity: String): List<WeatherResult> = listOf(
        WeatherResult(
            weatherApi.getWeatherCity(nameCity).weather.map {
                it.description
            }.toString(),
            weatherApi.getWeatherCity(nameCity).name,
            weatherApi.getWeatherCity(nameCity).main.temp,
            weatherApi.getWeatherCity(nameCity).main.humidity,
            weatherApi.getWeatherCity(nameCity).main.feelsLike,
            weatherApi.getWeatherCity(nameCity).weather.map {
                it.icon
            }.toString()
                .replace("[", "")
                .replace("]", ""),
            weatherApi.getWeatherCity(nameCity).wind.speed
        )
    )

    suspend fun getApiResultCoordinates(lat: Double, lon: Double): WeatherResult = WeatherResult(

        weatherApi.getWeatherCoordinates(lat, lon).weather.map {
            it.description
        }.toString(),
        weatherApi.getWeatherCoordinates(lat, lon).name,
        weatherApi.getWeatherCoordinates(lat, lon).main.temp,
        weatherApi.getWeatherCoordinates(lat, lon).main.humidity,
        weatherApi.getWeatherCoordinates(lat, lon).main.feelsLike,
        weatherApi.getWeatherCoordinates(lat, lon).weather.map {
            it.icon
        }.toString(),
        weatherApi.getWeatherCoordinates(lat, lon).wind.speed
    )

    fun getWeatherCityOutDatabase(): Flow<List<WeatherResult>> {
        return weatherDao.getWeatherCityList().map {
            it.map { weatherEntity ->
                WeatherResult(
                    weatherEntity.description,
                    weatherEntity.name,
                    weatherEntity.temp,
                    weatherEntity.humidity,
                    weatherEntity.feelsLike,
                    weatherEntity.iconId,
                    weatherEntity.windSpeed
                )
            }
        }
    }

    suspend fun addWeatherCityToDatabase(
        description: String,
        name: String,
        temp: Double,
        humidity: Int,
        feelsLike: Double,
        iconId: String,
        windSpeed: Double
    ) {
        val weatherCity = WeatherEntity(
            description,
            name,
            temp,
            humidity,
            feelsLike,
            iconId,
            windSpeed
        )
        weatherDao.addWeatherCity(weatherCity)
    }

    suspend fun deleteWeatherCityOutDatabase(weatherResult: WeatherResult) {

        weatherDao.deleteWeatherCity(
            WeatherEntity(
                weatherResult.description,
                weatherResult.name,
                weatherResult.temp,
                weatherResult.humidity,
                weatherResult.feelsLike,
                weatherResult.iconId,
                weatherResult.windSpeed
            )
        )
    }


    companion object {
        const val LANG = "ru"
        const val UNITS = "metric"
    }
}