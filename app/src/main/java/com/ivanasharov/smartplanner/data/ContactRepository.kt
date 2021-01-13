package com.ivanasharov.smartplanner.data

import com.ivanasharov.smartplanner.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAllContacts(): Flow<List<Contact>>
}