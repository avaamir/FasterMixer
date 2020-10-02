package com.behraz.fastermixer.batch.models.requests.openweathermap

import com.google.gson.annotations.SerializedName


data class Clouds (
	@SerializedName("all") val all : Float
)