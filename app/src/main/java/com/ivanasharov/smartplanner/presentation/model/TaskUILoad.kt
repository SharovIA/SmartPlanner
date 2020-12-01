package com.ivanasharov.smartplanner.presentation.model

data class TaskUILoad(val name : String,
    val description : String?,
    val date  : String,
    val timeFrom  : String,
    val timeTo  : String,
    val importance : String?,
    val address : String?,
    val contact : String?,
    val status : Boolean
)