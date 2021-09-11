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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.location.LocationRequest
import org.koin.core.component.KoinApiExtension
import java.text.ParseException

class WeatherWorkManager(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private lateinit var mLocation: Location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    override fun doWork(): Result {

        Log.d(TAG, "doWork: Done")
        Log.d(TAG, "onStartJob: STARTING JOB..")
        val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val c: Calendar = Calendar.getInstance()
        val date: Date = c.time
        val formattedDate: String = dateFormat.format(date)

        try {
            val currentDate = dateFormat.parse(formattedDate)
            val startDate = dateFormat.parse(DEFAULT_START_TIME)
            val endDate = dateFormat.parse(DEFAULT_END_TIME)

            if (currentDate!!.after(startDate) && currentDate.before(endDate)) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
                mLocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                    }
                }

                val mLocationRequest = LocationRequest.create()
                mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
                mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                try {
                    mFusedLocationClient
                        .lastLocation
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful && task.result != null) {

                                mLocation = task.result

                                ApiCoordinatesRepository.update(mLocation.latitude,mLocation.longitude)

                                mFusedLocationClient.removeLocationUpdates(mLocationCallback)

                            }
                        }

                } catch (e: SecurityException) {
                    e.printStackTrace()
                }

                try {
                    if (ActivityCompat.checkSelfPermission(
                            appContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            appContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, null)
                    }

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, null)

                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        } catch (e: ParseException) {

        }
        return Result.success()
    }

    companion object {

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
        private const val DEFAULT_START_TIME = "00:01"
        private const val DEFAULT_END_TIME = "23:59"
        private const val TAG = "MyWorker"
    }
}
