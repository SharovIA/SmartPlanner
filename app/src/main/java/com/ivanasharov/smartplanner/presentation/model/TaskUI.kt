package com.ivanasharov.smartplanner.presentation.model

import androidx.lifecycle.MutableLiveData

data class TaskUI(
    val name : MutableLiveData<String?> = MutableLiveData(null),
    val description : MutableLiveData<String?> = MutableLiveData(null),
    val date  : MutableLiveData<String?> = MutableLiveData( null),
    val timeFrom  : MutableLiveData<String?> = MutableLiveData(null),
    val timeTo  : MutableLiveData<String?> = MutableLiveData(null),
    val importance : MutableLiveData<String?> = MutableLiveData(null),
    val address : MutableLiveData<String?> = MutableLiveData(null),
    val contact : MutableLiveData<String?> = MutableLiveData(null),
    val status : MutableLiveData<Boolean> = MutableLiveData(false)
)