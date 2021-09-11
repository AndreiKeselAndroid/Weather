package com.google.andreikesel.weather.repository

import androidx.lifecycle.MutableLiveData
import com.google.andreikesel.weather.data.Location

object ApiCoordinatesRepository {

    private var location:Location? = null
    var liveData:MutableLiveData<Location> = MutableLiveData()

    fun update(lat:Double,lon:Double){
        location = Location(lat,lon)
        liveData.postValue(this.location)
    }
}