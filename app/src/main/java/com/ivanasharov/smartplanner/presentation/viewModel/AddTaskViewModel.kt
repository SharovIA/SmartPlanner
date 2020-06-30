package com.ivanasharov.smartplanner.presentation.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModel
import com.ivanasharov.smartplanner.presentation.view.MainActivity
import android.app.DatePickerDialog
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*


class AddTaskViewModel(@NonNull application : Application) : AndroidViewModel(application) {

    var day  : Int
    var month  : Int
    var year  : Int
    var date  : MutableLiveData<String> = MutableLiveData()
    var hours1  : Int
    var minutes1  : Int
    var timeFrom  : MutableLiveData<String> = MutableLiveData()
    var hours2  : Int
    var minutes2  : Int
    var timeTo  : MutableLiveData<String> = MutableLiveData()
    var name : MutableLiveData<String?> = MutableLiveData()
    var description : MutableLiveData<String?>  = MutableLiveData()
    var importance : MutableLiveData<String?> = MutableLiveData()
    var address : MutableLiveData<String?> = MutableLiveData()
    var isShowMap : MutableLiveData<Boolean> = MutableLiveData()
    var isSnapContect : MutableLiveData<Boolean> = MutableLiveData()
    var isAddToCalendar : MutableLiveData<Boolean> = MutableLiveData()
    var contact : MutableLiveData<String> = MutableLiveData()


    init {
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)
        hours1 = calendar.get(Calendar.HOUR)
        minutes1 = calendar.get(Calendar.MINUTE)
        hours2 = calendar.get(Calendar.HOUR)
        minutes2 = calendar.get(Calendar.MINUTE)
        name.value =null
        description.value = null
        importance.value = null
        address.value = null
        isShowMap.value = false
        isSnapContect.value = false
        isAddToCalendar.value = false
        contact.value = null
    }

    fun updateFullDate() { date.value = "$day-$month-$year"
    }

    fun updateFullTimeFrom() { timeFrom.value = getTextValue(hours1) +":" + getTextValue(minutes1)
    }

    fun updateFullTimeTo() { timeTo.value = getTextValue(hours2) +":" + getTextValue(minutes2)
    }



    private fun getTextValue(number :Int) : String{
        when(number<10) {
            true -> return "0$number"
            false -> return "$number"
        }
    }

    fun save() {
        TODO()
    }
    //public fun updateFullDate() { date.value. = date()}

}