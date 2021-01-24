package com.ivanasharov.smartplanner.data.repositories.contacts

import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAllContacts(): Flow<List<Contact>>
}