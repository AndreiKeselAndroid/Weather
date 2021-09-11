package com.google.andreikesel.weather.repository

import androidx.lifecycle.MutableLiveData
import com.google.andreikesel.weather.data.Location

object ApiCoordinatesRepository {

    var location: Location? = null
    var liveData: MutableLiveData<Location> = MutableLiveData()
    var isLocation:Boolean = true

    fun update(lat: Double, lon: Double) {
        if (isLocation){
            location = Location(lat, lon)
            liveData.postValue(this.location)
        }else{
            val _location = location
            liveData.postValue(_location)
        }
    }
}