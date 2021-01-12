package com.ivanasharov.smartplanner.presentation.model

import android.graphics.Color
import androidx.lifecycle.MutableLiveData

/*
data class Hour(
    var time: String,
    var color: Int,
    var colorMinutes: MutableList<Int>? //= Array(60) {Color.RED}
) */
data class Hour(
    var time: String,
    var color: Int,
    var colorMinutes: MutableList<Int>?//val colorMinutes: MutableLiveData<MutableList<Int>?> = MutableLiveData(null) //= Array(60) {Color.RED}
)