package com.google.andreikesel.weather.screen.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentMainBinding
import com.google.andreikesel.weather.models.MainViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private val viewModel: MainViewModel by viewModel()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var myLocation: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    @SuppressLint("RestrictedApi", "UnspecifiedImmutableFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                myLocation = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)
                viewModel.update(myLocation!!.latitude, myLocation!!.longitude, binding!!)
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ), 2
            )
        } else {
            locationWizardry()
        }

        binding?.searchImage?.setOnClickListener {
            view.findNavController().navigate(R.id.searchFragment)
        }

        binding?.cityManagerImage?.setOnClickListener {
            view.findNavController().navigate(R.id.managerCityFragment)
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
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            myLocation = LatLng(location.latitude, location.longitude)

        }

        createLocRequest()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(requireActivity(), 500)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 20000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}