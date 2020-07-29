package com.ivanasharov.smartplanner.data.entity

import android.location.Address
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ivanasharov.smartplanner.data.GregorianCalendarConverter
import java.sql.Time
import java.util.*

@Entity
@TypeConverters(GregorianCalendarConverter::class)
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
    @Embedded
    val address: Address,
    val isShowMap: Boolean? = false,
    val contact : String? = null,
    val status : Boolean = false
){

    data class Address(
        val country : String? = null,
        val region : String? =null,
        val town : String? = null,
        val street : String? = null,
        val house : String? = null
    )
}