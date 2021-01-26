package com.ivanasharov.smartplanner.data.clients.dto

import com.google.gson.annotations.SerializedName

data class Wind (

	@SerializedName("speed")
	val speed : Double,
	@SerializedName("deg")
	val deg : Int
)