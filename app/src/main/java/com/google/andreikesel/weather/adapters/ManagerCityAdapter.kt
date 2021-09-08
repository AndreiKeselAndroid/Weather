package com.google.andreikesel.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.andreikesel.R
import com.google.andreikesel.databinding.ItemCityBinding
import com.google.andreikesel.weather.data.WeatherResult

class ManagerCityAdapter(

    private val deleteCity: (WeatherResult) -> Unit

) : ListAdapter<WeatherResult,
        ManagerCityAdapter.ManagerCityViewHolder>(DifUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManagerCityViewHolder =

        ManagerCityViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            deleteCity
        )

    override fun onBindViewHolder(holder: ManagerCityViewHolder, position: Int) {
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

    class ManagerCityViewHolder(
        private val bindingView: ItemCityBinding,
        private val deleteCity: (WeatherResult) -> Unit

    ) : RecyclerView.ViewHolder(bindingView.root) {

        fun bind(item: WeatherResult) {

            bindingView.tvHumidity.text = item.humidity.toString()
            bindingView.tvTemp.text = item.temp.toString()
            bindingView.tvWind.text = item.windSpeed.toString()
            bindingView.tvName.text = item.name

            bindingView.itemCity.setOnLongClickListener {

                deleteCity(item)

                return@setOnLongClickListener true
            }

            Glide
                .with(bindingView.root)
                .load(item.iconId)
                .placeholder(R.drawable.ic_cloudy)
                .into(bindingView.weatherImage)
        }
    }
}