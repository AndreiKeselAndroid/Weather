package com.google.andreikesel.weather.database.dao

import androidx.room.*
import com.google.andreikesel.weather.database.entity.SavedCityWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedCityWeatherDao {

    @Query("SELECT * FROM table_saved_city_weather WHERE  ID = (SELECT MAX(ID)  FROM table_saved_city_weather)")
    fun getSavedCityWeather(): Flow<SavedCityWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavedCityWeather(savedCityWeatherEntity: SavedCityWeatherEntity)
}