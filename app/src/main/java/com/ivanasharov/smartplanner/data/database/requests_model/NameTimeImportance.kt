package com.ivanasharov.smartplanner.data.database.requests_model

import java.util.*

data class NameTimeImportance(
    val name: String,
    val timeFrom: GregorianCalendar,
    val timeTo: GregorianCalendar,
    val importance: Int?
)