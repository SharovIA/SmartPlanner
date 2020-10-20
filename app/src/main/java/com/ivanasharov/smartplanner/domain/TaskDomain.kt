package com.ivanasharov.smartplanner.domain

import java.util.*

data class TaskDomain(
    val name : String?,
    val description : String?,
    val date : GregorianCalendar?,
    val timeFrom : GregorianCalendar?,
    val timeTo : GregorianCalendar?,
    val importance: Int,
    val address: String?,
    val isAddContact : Boolean?,
    val contact : String?,
    val isAddCalendar : Boolean?,
    var status : Boolean
)