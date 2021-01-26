package com.ivanasharov.smartplanner.presentation.view.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ivanasharov.smartplanner.R

class PermissionDialogFragment(
    private val mListener: PermissionDialogListener
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
                    mListener.onDialogPositiveClick()
                }
                .setNegativeButton(getString(R.string.deny)) { _, _ ->
                    mListener.onDialogNegativeClick()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}