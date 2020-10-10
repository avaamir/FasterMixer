package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.view.View
import android.view.WindowManager
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.requests.openweathermap.WeatherViewData
import com.behraz.fastermixer.batch.utils.general.loadWeatherIcon
import kotlinx.android.synthetic.main.item_weather.*

class WeatherDialog(context: Context, themeResId: Int,private val weatherViewData: WeatherViewData) : MyBaseFullScreenDialog(context, themeResId, R.layout.layout_dilaog_weather) {

    override fun initViews() {
        tvCurrentTemp.text = weatherViewData.temp
        tvDescription.text = weatherViewData.description
        tvTime.text = weatherViewData.time
        tvDate.text = weatherViewData.date
        tvDescription.text = weatherViewData.description
        tvMaxTemp.text = weatherViewData.maxTemp
        tvMinTemp.text = weatherViewData.minTemp
        loadWeatherIcon(imageView9, weatherViewData.iconURL)
    }
 
}