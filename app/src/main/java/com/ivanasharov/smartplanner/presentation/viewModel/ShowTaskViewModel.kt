package com.ivanasharov.smartplanner.presentation.viewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import com.ivanasharov.smartplanner.presentation.viewModel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ShowTaskViewModel @Inject constructor(): BaseViewModel() {

    val taskUI = TaskUI()

    init {
        taskUI.isAddToCalendar.value =false
    }

   private fun longToCalendar(timeInMillis : Long?) : GregorianCalendar?{
        if (timeInMillis != null) {
            val calendar: Calendar = Calendar.getInstance()
            if (timeInMillis != null) calendar.timeInMillis = timeInMillis
            return GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)
            )
        }
        else return null
    }

    fun init(intent: Intent) {
        taskUI.name.value = intent.getStringExtra("name")
        taskUI.description.value = intent.getStringExtra("description")
        taskUI.date.value = longToCalendar(intent.getLongExtra("date", 0))
        taskUI.timeFrom.value = intent.getStringExtra("timeFrom")
        taskUI.timeTo.value = intent.getStringExtra("timeTo")
        taskUI.importance.value = intent.getStringExtra("importance")
        taskUI.address.value = intent.getStringExtra("address")
        taskUI.isSnapContact.value = intent.getBooleanExtra("isSnapContact", false)
        taskUI.contact.value = intent.getStringExtra("contact")
        taskUI.status.value = intent.getBooleanExtra("status", false)
    }

}