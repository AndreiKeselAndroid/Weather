package com.google.andreikesel.weather.screen.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.andreikesel.databinding.FragmentSearchBinding
import com.google.andreikesel.weather.adapters.ManagerCityAdapter
import com.google.andreikesel.weather.adapters.SearchAdapter
import com.google.andreikesel.weather.data.WeatherResult
import com.google.andreikesel.weather.models.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backImage?.setOnClickListener {
            view.findNavController().popBackStack()
        }

        val weatherCityAdapter = SearchAdapter(
            ::addCity
        )

        with(binding!!.rvSearchCity) {
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

        binding?.btnSearch?.setOnClickListener {

            if (binding!!.etSearch.text.isNotEmpty()) {
                viewModel.searchCity(binding!!.etSearch.text.toString())
            }
        }
    }

    private fun addCity(weatherResult: WeatherResult) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}