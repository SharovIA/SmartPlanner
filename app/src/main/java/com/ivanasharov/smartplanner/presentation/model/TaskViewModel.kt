package com.ivanasharov.smartplanner.presentation.model

import java.io.Serializable

/*data class TaskUI(
        val name : MutableLiveData<String?> = MutableLiveData(),
        val description : MutableLiveData<String?> = MutableLiveData(),
        val date  : MutableLiveData<GregorianCalendar> = MutableLiveData(),
        val timeFrom  : MutableLiveData<String?> = MutableLiveData(),
        val timeTo  : MutableLiveData<String?> = MutableLiveData(),
        val importance : MutableLiveData<String?> = MutableLiveData(),
        val address : MutableLiveData<String?> = MutableLiveData(),
        val isSnapContact : MutableLiveData<Boolean> = MutableLiveData(),
        val contact : MutableLiveData<String?> = MutableLiveData(),
        val isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData(),
        val status : MutableLiveData<Boolean> = MutableLiveData()
)*/

/*data class NewTaskUI(
        var name : String? = "test",
        var description : String? =null,
        var date  : String? =null,
        var timeFrom  : String? =null,
        var timeTo  : String? = null,
        var importance : String? =null,
        var address : String? =null,
        var isSnapContact : Boolean =false,
        var contact : String? =null,
        var isAddToCalendar : Boolean=false,
        var status : Boolean=false
)*/
/*
data class NewTaskUI(
        val name : String,
        val description : String?,
        val date  : String?,
        val timeFrom  : String?,
        val timeTo  : String?,
        val importance : String?,
        val address : String?,
        val isSnapContact : Boolean,
        val contact : String?,
        val isAddToCalendar : Boolean,
        val status : Boolean
)
*/
/*
data class NewTaskUI(
        val name : MutableLiveData<String?> = MutableLiveData(null),
        val description : MutableLiveData<String?> = MutableLiveData(null),
        val date  : MutableLiveData<String?> = MutableLiveData( null),
        val timeFrom  : MutableLiveData<String?> = MutableLiveData(null),
        val timeTo  : MutableLiveData<String?> = MutableLiveData(null),
        val importance : MutableLiveData<String?> = MutableLiveData(null),
        val address : MutableLiveData<String?> = MutableLiveData(null),
        val isSnapContact : MutableLiveData<Boolean> = MutableLiveData(false),
        val contact : MutableLiveData<String?> = MutableLiveData(null),
        val isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData(false),
        val status : MutableLiveData<Boolean> = MutableLiveData(false)
)*/

data class TaskViewModel( val taskUILoad: TaskUILoad,
                          val name : String = taskUILoad.name,
                          val description : String? = taskUILoad.description,
                          val date  : String = taskUILoad.date,
                          val timeFrom  : String = taskUILoad.timeFrom,
                          val timeTo  : String = taskUILoad.timeTo,
                          val importance : String? = taskUILoad.importance,
                          val address : String? = taskUILoad.address,
                          val contact : String? = taskUILoad.contact,
                          val status : Boolean = taskUILoad.status
): Serializable