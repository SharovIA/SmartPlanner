package com.ivanasharov.smartplanner.shared.model

data class Contact(
    private val id : String,
    private val name : String,
    private val phone : String
){
    fun getData(): String {
        return "$name\n $phone"
    }
}