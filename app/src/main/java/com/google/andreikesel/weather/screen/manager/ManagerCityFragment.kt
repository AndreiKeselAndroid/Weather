package com.google.andreikesel.weather.screen.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.andreikesel.databinding.FragmentManagerCityBinding
import com.google.andreikesel.weather.adapters.ManagerCityAdapter
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.models.ManagerCityViewModel
import com.google.andreikesel.weather.models.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            view.findNavController().popBackStack()
        }

        val weatherCityAdapter = ManagerCityAdapter(
            ::deleteCity
        )

        with(binding!!.rvManagerCityContainer) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = weatherCityAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun deleteCity(weatherResult: WeatherResult) {

    }
}