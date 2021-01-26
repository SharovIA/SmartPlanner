package com.ivanasharov.smartplanner.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id : Long?,
    val name : String? = null,
    val description : String? = null,
    val date: GregorianCalendar? = null,
    val timeFrom: GregorianCalendar? = null,
    val timeTo: GregorianCalendar? = null,
    val importance : Int = 0,
    val isLocation: Boolean = false,
    val address: String?,
    val contact : String? = null,
    val status : Boolean = false
)