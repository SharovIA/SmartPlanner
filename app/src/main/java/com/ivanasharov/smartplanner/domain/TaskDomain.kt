package com.ivanasharov.smartplanner.domain

data class TaskDomain(
    val name : String?,
    val description : String?,
    val date : String?,
    val timeFrom : String?,
    val timeTo : String?,
    val importance: Int,
    val address: String?,
    val isShowMap : Boolean?,
    val isAddContact : Boolean?,
    val contact : String?,
    val isAddCalendar : Boolean?
)