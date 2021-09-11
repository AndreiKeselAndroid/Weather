package com.google.andreikesel.weather.screen.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentManagerCityBinding
import com.google.andreikesel.weather.adapters.ManagerCityAdapter
import com.google.andreikesel.weather.data.Location
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.models.ManagerCityViewModel
import com.google.andreikesel.weather.repository.ApiCoordinatesRepository
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ManagerCityFragment : Fragment() {

    private var binding: FragmentManagerCityBinding? = null
    private val viewModel: ManagerCityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentManagerCityBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backImage?.setOnClickListener {
            view.findNavController().popBackStack(R.id.mainFragment,false)
        }

        val weatherCityAdapter = ManagerCityAdapter(
            ::deleteCity,
            ::insertCityFromDatabase
        )

        with(binding!!.rvManagerCityContainer) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = weatherCityAdapter
        }

        viewModel.liveData.observe(viewLifecycleOwner, {
            weatherCityAdapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun deleteCity(weatherResult: WeatherResult) {
        viewModel.deleteCity(weatherResult)
    }

    private fun insertCityFromDatabase(weatherResult: WeatherResult) {
        ApiCoordinatesRepository.isLocation = false
        ApiCoordinatesRepository.location = Location(weatherResult.lat,weatherResult.lon)
        view?.findNavController()?.popBackStack(R.id.mainFragment,false)
    }
}