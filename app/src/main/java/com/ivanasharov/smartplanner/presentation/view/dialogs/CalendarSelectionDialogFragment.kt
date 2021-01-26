package com.ivanasharov.smartplanner.presentation.view.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import kotlinx.coroutines.NonCancellable.cancel

class CalendarSelectionDialogFragment(
    private val mAddTaskViewModel: AddTaskViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val namesOfCalendaesArray = mAddTaskViewModel.namesCalendarsList.value?.toTypedArray() as Array
            builder.setTitle(getString(R.string.selection_calendar))
                .setItems(namesOfCalendaesArray) { _, which ->
                    mAddTaskViewModel.nameOfCalendar.value = namesOfCalendaesArray[which]
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}