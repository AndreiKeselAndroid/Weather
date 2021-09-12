package com.google.andreikesel.weather.screen.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.andreikesel.R
import com.google.andreikesel.databinding.FragmentSettingsBinding
import com.google.andreikesel.weather.repository.ApiCoordinatesRepository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private var alarmDateStart: Calendar = Calendar.getInstance()
    private var alarmDateEnd: Calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefTime()

        binding?.timePickerStart?.setOnTimeChangedListener { view, hourOfDay, minute ->
            alarmDateStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
            alarmDateStart.set(Calendar.MINUTE, minute)
        }

        binding?.timePickerEnd?.setOnTimeChangedListener { view, hourOfDay, minute ->
            alarmDateEnd.set(Calendar.HOUR_OF_DAY, hourOfDay)
            alarmDateEnd.set(Calendar.MINUTE, minute)
        }

        binding?.btnStart?.setOnClickListener {

            setTime()
            view.findNavController().navigate(R.id.mainFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDefTime() {

        binding?.timePickerStart?.setIs24HourView(true)
        binding?.timePickerStart?.hour = HOUR_START
        binding?.timePickerStart?.minute = MINUTE_START
        binding?.timePickerEnd?.setIs24HourView(true)
        binding?.timePickerEnd?.hour = HOUR_END
        binding?.timePickerEnd?.minute = MINUTE_END
        alarmDateStart.set(Calendar.HOUR_OF_DAY, HOUR_START)
        alarmDateStart.set(Calendar.MINUTE, MINUTE_START)
        alarmDateEnd.set(Calendar.HOUR_OF_DAY, HOUR_END)
        alarmDateEnd.set(Calendar.MINUTE, MINUTE_END)
    }

    private fun setTime() {

        val dateFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateStart: Date = alarmDateStart.time
        val dateEnd: Date = alarmDateEnd.time
        val formattedDateStart: String = dateFormat.format(dateStart)
        val formattedDateEnd: String = dateFormat.format(dateEnd)
        ApiCoordinatesRepository.time_start = formattedDateStart
        ApiCoordinatesRepository.time_end = formattedDateEnd
    }

    companion object {
        private const val HOUR_START = 0
        private const val MINUTE_START = 0
        private const val HOUR_END = 23
        private const val MINUTE_END = 59
    }
}