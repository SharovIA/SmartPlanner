package com.ivanasharov.smartplanner.presentation.model

data class Hour(
    var time: String,
    var color: Int,
    var colorMinutes: MutableList<Int>?
)