package com.google.andreikesel.weather.screen.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.andreikesel.BR
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentMainBinding
import com.google.andreikesel.weather.models.MainViewModel
import com.google.andreikesel.weather.repository.ApiCoordinatesRepository
import com.google.andreikesel.weather.workManagers.WeatherWorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit


@KoinApiExtension
class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this
        binding?.setVariable(BR.viewmodel, viewModel)

        return binding!!.root
    }

    @SuppressLint("RestrictedApi", "UnspecifiedImmutableFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()

        createAndStartWorkManager()

        binding?.searchImage?.setOnClickListener {
            view.findNavController().navigate(R.id.searchFragment)
        }

        binding?.cityManagerImage?.setOnClickListener {
            view.findNavController().navigate(R.id.managerCityFragment)
        }

        binding?.ivLocation?.setOnClickListener {

            getMyLocationAndUpdate()
        }

        if (!checkLocationPermission()) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocationAndUpdate() {
        ApiCoordinatesRepository.isLocation = true
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    ApiCoordinatesRepository.update(it.latitude, it.longitude)
                } else {
                    viewModel.updateIfLocationNull(binding!!)
                }
            }
    }

    private fun createAndStartWorkManager() {

        val uploadWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<WeatherWorkManager>(15, TimeUnit.MINUTES)
                .build()

        WorkManager
            .getInstance(requireContext())
            .enqueue(uploadWorkRequest)
    }

    private fun update() {

        ApiCoordinatesRepository.liveData.observe(viewLifecycleOwner, {
            when (ApiCoordinatesRepository.isLocation) {
                true -> {
                    viewModel.update(it.latitude, it.longitude, binding!!)
                }
                false -> {
                    viewModel.update(
                        ApiCoordinatesRepository.location!!.latitude,
                        ApiCoordinatesRepository.location!!.longitude, binding!!
                    )
                }
            }
        })

        if (ApiCoordinatesRepository.location == null) {
            viewModel.updateIfLocationNull(binding!!)
        }
    }

    private fun checkLocationPermission(): Boolean {
        val chekCoarse = ContextCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION)
        val chekFine = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
        return chekCoarse == PackageManager.PERMISSION_GRANTED &&
                chekFine == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (coarseLocation && fineLocation) Toast.makeText(
                    requireContext(),
                    R.string.permission_granted,
                    Toast.LENGTH_SHORT
                ).show() else {
                    Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }
}
