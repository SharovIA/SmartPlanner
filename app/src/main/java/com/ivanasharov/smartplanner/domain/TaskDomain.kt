package com.ivanasharov.smartplanner.domain

import java.util.*

data class TaskDomain(
    val name : String?,
    val description : String?,
   // val date : String?,
 //   val date : Calendar?,
    val date : GregorianCalendar?,
    val timeFrom : GregorianCalendar?,
    val timeTo : GregorianCalendar?,
    val importance: Int,
    val address: String?,
    val isShowMap : Boolean?,
    val isAddContact : Boolean?,
    val contact : String?,
    val isAddCalendar : Boolean?
)