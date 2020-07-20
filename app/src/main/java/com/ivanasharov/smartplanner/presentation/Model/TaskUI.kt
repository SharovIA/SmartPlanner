package com.ivanasharov.smartplanner.presentation.Model

import androidx.lifecycle.MutableLiveData

data class TaskUI(
        val name : MutableLiveData<String?> = MutableLiveData(),
        val description : MutableLiveData<String?> = MutableLiveData(),
        val date  : MutableLiveData<String?> = MutableLiveData(),
        val timeFrom  : MutableLiveData<String?> = MutableLiveData(),
        val timeTo  : MutableLiveData<String?> = MutableLiveData(),
        val importance : MutableLiveData<String?> = MutableLiveData(),
        val address : MutableLiveData<String?> = MutableLiveData(),
        val isShowMap : MutableLiveData<Boolean> = MutableLiveData(),
        val isSnapContact : MutableLiveData<Boolean> = MutableLiveData(),
        val contact : MutableLiveData<String?> = MutableLiveData(),
        val isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData()
)