package com.google.andreikesel.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.andreikesel.R
import com.google.andreikesel.databinding.ActivityMainBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var myLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                myLocation = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ), 2
            )
        } else {
            locationWizardry()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationWizardry()
    }

    @SuppressLint("MissingPermission")
    private fun locationWizardry() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                myLocation = LatLng(location.latitude, location.longitude)
            }
        }

        createLocRequest()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, 500)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocRequest() {
        locationRequest = LocationRequest.create()
//        locationRequest.interval = 10000 //time in ms; every ~10 seconds
//        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}