package com.google.andreikesel.weather.repository

import com.google.andreikesel.weather.cloud.WeatherApi
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.database.dao.SavedCityWeatherDao
import com.google.andreikesel.weather.database.dao.WeatherDao
import com.google.andreikesel.weather.database.entity.SavedCityWeatherEntity
import com.google.andreikesel.weather.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApiRepository(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val savedCityWeatherDao: SavedCityWeatherDao
) {
    suspend fun getApiResultCity(nameCity: String): List<WeatherResult> = listOf(
        WeatherResult(
            weatherApi.getWeatherCity(nameCity).coord.lat,
            weatherApi.getWeatherCity(nameCity).coord.lon,
            removeChars(weatherApi.getWeatherCity(nameCity).weather.map {
                it.description
            }.toString()),
            weatherApi.getWeatherCity(nameCity).name,
            weatherApi.getWeatherCity(nameCity).main.temp.toInt(),
            weatherApi.getWeatherCity(nameCity).main.humidity,
            weatherApi.getWeatherCity(nameCity).main.feelsLike,
            removeChars(weatherApi.getWeatherCity(nameCity).weather.map {
                removeChars(it.icon)
            }.toString()),
            weatherApi.getWeatherCity(nameCity).wind.speed
        )
    )

    fun getWeatherCityOutDatabase(): Flow<List<WeatherResult>> {
        return weatherDao.getWeatherCityList().map {
            it.map { weatherEntity ->
                WeatherResult(
                    weatherEntity.lat,
                    weatherEntity.lon,
                    removeChars(weatherEntity.description),
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
        lat: Double,
        lon: Double,
        description: String,
        name: String,
        temp: Int,
        humidity: Int,
        feelsLike: Double,
        iconId: String,
        windSpeed: Double
    ) {
        val weatherCity = WeatherEntity(
            lat,
            lon,
            removeChars(description),
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
        )
    }


    fun getSavedCityWeatherOutDatabase(): Flow<WeatherResult> {
        return savedCityWeatherDao.getSavedCityWeather().map { savedCityWeatherEntity ->
            WeatherResult(
                savedCityWeatherEntity.lat,
                savedCityWeatherEntity.lon,
                removeChars(savedCityWeatherEntity.description),
                savedCityWeatherEntity.name,
                savedCityWeatherEntity.temp,
                savedCityWeatherEntity.humidity,
                savedCityWeatherEntity.feelsLike,
                savedCityWeatherEntity.iconId,
                savedCityWeatherEntity.windSpeed
            )
        }
    }

    suspend fun addSavedCityWeatherToDatabase(lat: Double, lon: Double) {
        val savedWeatherCity = SavedCityWeatherEntity(
            lat = weatherApi.getWeatherCoordinates(lat,lon).coord.lat,
            lon = weatherApi.getWeatherCoordinates(lat,lon).coord.lon,
            description = removeChars(weatherApi.getWeatherCoordinates(lat, lon).weather.map {
                it.description
            }.toString()),
            name = weatherApi.getWeatherCoordinates(lat, lon).name,
            temp = weatherApi.getWeatherCoordinates(lat, lon).main.temp.toInt(),
            humidity = weatherApi.getWeatherCoordinates(lat, lon).main.humidity,
            feelsLike = weatherApi.getWeatherCoordinates(lat, lon).main.feelsLike,
            iconId = removeChars(weatherApi.getWeatherCoordinates(lat, lon).weather.map {
                it.icon
            }.toString()),
            windSpeed = weatherApi.getWeatherCoordinates(lat, lon).wind.speed
        )

        savedCityWeatherDao.addSavedCityWeather(savedWeatherCity)
    }

    private fun removeChars(s: String) = s.replace("[", "").replace("]", "")

    companion object {
        const val LANG = "ru"
        const val UNITS = "metric"
    }
}