package com.ivanasharov.smartplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var name : String? = null,
    var description : String? = null,
    var date: Long = 0,
    var timeFrom: Long = 0,
    var timeTo: Long = 0,
    var importance : Int = 0,
    var isLocation: Boolean = false,
    var contact : String? = null,
    var status : Boolean = false
)