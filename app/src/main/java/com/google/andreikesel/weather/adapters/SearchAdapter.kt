package com.google.andreikesel.weather.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.andreikesel.R
import com.google.andreikesel.databinding.ItemCityBinding
import com.google.andreikesel.weather.data.WeatherResult

class SearchAdapter(

    private val addCity: (WeatherResult) -> Unit

) : ListAdapter<WeatherResult,
        SearchAdapter.SearchCityViewHolder>(DifUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchCityViewHolder =

        SearchCityViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            addCity
        )

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DifUtilItemCallback : DiffUtil.ItemCallback<WeatherResult>() {

        override fun areItemsTheSame(
            oldItem: WeatherResult,
            newItem: WeatherResult
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeatherResult,
            newItem: WeatherResult
        ): Boolean {
            return oldItem.description == newItem.description
                    && oldItem.name == newItem.name
                    && oldItem.feelsLike == newItem.feelsLike
                    && oldItem.humidity == newItem.humidity
                    && oldItem.iconId == newItem.iconId
                    && oldItem.temp == newItem.temp
                    && oldItem.windSpeed == newItem.windSpeed

        }
    }

    class SearchCityViewHolder(
        private val bindingView: ItemCityBinding,
        private val addCity: (WeatherResult) -> Unit

    ) : RecyclerView.ViewHolder(bindingView.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: WeatherResult) {

            bindingView.tvHumidity.text = item.humidity.toString()
            bindingView.tvTemp.text = item.temp.toString()
            bindingView.tvWind.text = item.windSpeed.toString()+ " м/с"
            bindingView.tvName.text = item.name

            bindingView.itemCity.setOnClickListener {

                addCity(item)
            }

            val url = "https://openweathermap.org/img/wn/${item.iconId}@2x.png"

            Glide
                .with(bindingView.root)
                .load(url)
                .into(bindingView.weatherImage)
        }
    }
}