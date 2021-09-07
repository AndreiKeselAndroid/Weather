package com.google.andreikesel.weather.database.dao

import androidx.room.*
import com.google.andreikesel.weather.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM table_weather_info")
    fun getWeatherCityList(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeatherCity(weatherEntity: WeatherEntity)

    @Delete
    suspend fun deleteWeatherCity(weatherEntity: WeatherEntity)
}