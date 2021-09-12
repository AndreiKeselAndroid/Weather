package com.google.andreikesel.weather.workManagers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.andreikesel.weather.repository.ApiCoordinatesRepository
import com.google.android.gms.location.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class WeatherWorkManager(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private lateinit var mLocation: Location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    override fun doWork(): Result {

        val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val c: Calendar = Calendar.getInstance()
        val date: Date = c.time
        val formattedDate: String = dateFormat.format(date)

        try {

            val currentDate = dateFormat.parse(formattedDate)
            val startTime = ApiCoordinatesRepository.time_start
            val endTime = ApiCoordinatesRepository.time_end
            val startDate = dateFormat.parse(startTime!!)
            val endDate = dateFormat.parse(endTime!!)

            if (currentDate!!.after(startDate) && currentDate.before(endDate)) {

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
                createLocationCallback()
                val mLocationRequest = LocationRequest.create()
                mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                addOnCompleteListener()
                checkSelfPermission(mLocationRequest)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Result.success()
    }

    private fun addOnCompleteListener() {

        try {

            mFusedLocationClient
                .lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {

                        mLocation = task.result

                        ApiCoordinatesRepository.update(
                            mLocation.latitude,
                            mLocation.longitude
                        )

                        mFusedLocationClient.removeLocationUpdates(mLocationCallback)

                    }
                }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun createLocationCallback() {

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
            }
        }
    }

    private fun checkSelfPermission(mLocationRequest: LocationRequest) {

        try {

            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    PENDING_INTENT
                )
            }

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, PENDING_INTENT)

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2 * 60 * 1000
        private val PENDING_INTENT = null
    }
}