package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class Wind (
	@SerializedName("speed") val speed : Float,
	@SerializedName("deg") val degree : Float
)