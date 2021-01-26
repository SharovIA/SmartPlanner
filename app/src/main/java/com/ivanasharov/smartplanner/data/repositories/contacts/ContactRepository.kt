package com.ivanasharov.smartplanner.data.repositories.contacts

import com.ivanasharov.smartplanner.shared.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAllContacts(): Flow<List<Contact>>
}