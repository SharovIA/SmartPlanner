package com.ivanasharov.smartplanner.presentation.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import kotlinx.coroutines.NonCancellable.cancel

class PermissionDialogFragment(
    private val listener: PermissionDialogListener
) : DialogFragment() {

    interface PermissionDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.location_request_explanation))
                .setPositiveButton(getString(R.string.grant)) { _, _ ->
                    listener.onDialogPositiveClick()
                }
                .setNegativeButton(getString(R.string.deny)) { _, _ ->
                    listener.onDialogNegativeClick()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}