package com.google.andreikesel.weather.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding:FragmentMainBinding? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding?.searchImage?.setOnClickListener {
            view.findNavController().navigate(R.id.searchFragment)
        }

        binding?.cityManagerImage?.setOnClickListener {
            view.findNavController().navigate(R.id.managerCityFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}