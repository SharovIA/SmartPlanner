package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.FragmentAddTaskBinding
import com.ivanasharov.smartplanner.presentation.view.dialogs.CalendarSelectionDialogFragment
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    companion object {
        private const val PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR = 123
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 124
    }

    private var mIsPermissionForReadContacts = false
    private var mIsPermissionForReadAndWriteCalendar = false

    private val mAddTaskViewModel: AddTaskViewModel by viewModels()
    private val mArguments: AddTaskFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentAddTaskBinding
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>

    private var mIsInitSpinnerFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mArguments.idOfTask != -1L) {
            mAddTaskViewModel.loadTaskForEdit(mArguments.idOfTask)
            mAddTaskViewModel.modeNewTask = false       //Edit mode
        }
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mAddTaskViewModel
        initSpinner()
        initListeners()

        return mBinding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTaskViewModel.isSave.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initListeners() {
        mBinding.dateTextView.setOnClickListener {
            setDate()
        }
        mBinding.time1TextView.setOnClickListener { setTime(true) }
        mBinding.time2TextView.setOnClickListener { setTime(false) }

        mBinding.addContactCheckBox.setOnClickListener {
            if (hasPermissionReadContacts()) {
                mIsPermissionForReadContacts = true
                initSpinnerContacts()
            } else {
                mIsPermissionForReadContacts = false
                requestPermissionWithRationale(PERMISSIONS_REQUEST_READ_CONTACTS)
            }
            mBinding.addContactCheckBox.isChecked = mIsPermissionForReadContacts
        }

        mBinding.addCalendarAndroidCheckBox.setOnClickListener {
            if (hasPermissionReadCalendar()) {
                mIsPermissionForReadAndWriteCalendar = true
                chooseCalendar()

            } else {
                mIsPermissionForReadAndWriteCalendar = false
                requestPermissionWithRationale(PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR)
            }
            mBinding.addCalendarAndroidCheckBox.isChecked = mIsPermissionForReadAndWriteCalendar
        }

        mBinding.saveButton.setOnClickListener {
            getDataFromActivity()
            mAddTaskViewModel.save()
        }

    }

    private fun getDataFromActivity() {
        if (mBinding.addContactCheckBox.isChecked) {
            mAddTaskViewModel.taskUI.contact.value = mBinding.contactSpinner.selectedItem.toString()
        }
    }

    private fun chooseCalendar() {
        mAddTaskViewModel.loadCalendars()
        mAddTaskViewModel.namesCalendarsList.observe(viewLifecycleOwner, Observer {
            initDialog()
        })

    }

    private fun hasPermissionReadCalendar(): Boolean {
        var result = 0
        val permissions =
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        for (permission in permissions) {
            result = checkCallingOrSelfPermission(requireContext(), permission)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun requestPermsForCalendar() {
        val permissions =
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var allowedReadAndWriteCalendar = false
        var allowedReadContacts = false

        when (requestCode) {
            PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR -> {
                allowedReadAndWriteCalendar = true
                for (res in grantResults) {
                    // if user granted all permissions.
                    allowedReadAndWriteCalendar =
                        allowedReadAndWriteCalendar && (res == PackageManager.PERMISSION_GRANTED)
                }
            }
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                allowedReadContacts = true
                allowedReadContacts =
                    allowedReadContacts && (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
            else -> {
                // if user not granted permissions.
                allowedReadContacts = false
                allowedReadAndWriteCalendar = false
            }
        }

        when (requestCode) {
            PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR -> {
                if (allowedReadAndWriteCalendar) {
                    mIsPermissionForReadAndWriteCalendar = true
                    mBinding.addCalendarAndroidCheckBox.isChecked = true
                    chooseCalendar()
                } else {
                    // we will give warning to user that they haven't granted permissions.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR)) {
                            Toast.makeText(
                                requireContext(),
                                "Read calendar permission denied.",
                                Toast.LENGTH_LONG
                            ).show();
                        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR)) {
                            Toast.makeText(
                                requireContext(),
                                "Write calendar permission denied.",
                                Toast.LENGTH_LONG
                            ).show();

                        } else {
                            showNoPermissionSnackbar(PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR);
                        }
                    }
                }
            }
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (allowedReadContacts) {
                    mIsPermissionForReadContacts = true
                    mBinding.addContactCheckBox.isChecked = true
                    initSpinnerContacts()
                } else {
                    // we will give warning to user that they haven't granted permissions.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                            Toast.makeText(
                                requireContext(),
                                "Contact Permissions denied.",
                                Toast.LENGTH_SHORT
                            ).show();

                        } else {
                            showNoPermissionSnackbar(PERMISSIONS_REQUEST_READ_CONTACTS)
                        }
                    }
                }
            }

        }
    }

    private fun showNoPermissionSnackbar(code: Int) {
        when (code) {
            PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR -> {
                Snackbar.make(
                    activity?.findViewById(android.R.id.content) as View,
                    "Read calendar and Write calendar permissions are not granted",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Settings", View.OnClickListener {
                        openApplicationSettings(code)
                        Toast.makeText(
                            requireContext(),
                            "Open Permissions and grant the read and write calendar permissions",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    })
                    .show()
            }
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                Snackbar.make(
                    activity?.findViewById(android.R.id.content) as View,
                    "Read contacts permission isn't granted",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Settings", View.OnClickListener {
                        openApplicationSettings(code)
                        Toast.makeText(
                            requireContext(),
                            "Open Permissions and grant the read contacts permission",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    })
                    .show()
            }
        }

    }

    private fun openApplicationSettings(code: Int) {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + activity?.packageName)
        )
        startActivityForResult(appSettingsIntent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR) {
            mIsPermissionForReadAndWriteCalendar = true
            mBinding.addCalendarAndroidCheckBox.isChecked = true
            chooseCalendar()
            return
        }
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            mIsPermissionForReadContacts = true
            mBinding.addContactCheckBox.isChecked = true
            initSpinnerContacts()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestPermissionWithRationale(code: Int) {
        when (code) {
            PERMISSIONS_REQUEST_READ_AND_WRITE_CALENDAR -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_CALENDAR
                    )
                ) {
                    val message =
                        "Read calendar and  write calendar permissions are needed to add task in Calendar Android"
                    Snackbar.make(
                        activity?.findViewById(android.R.id.content) as View,
                        message,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("GRANT") {
                            requestPermsForCalendar()
                        }.show()
                } else {
                    requestPermsForCalendar()
                }
            }
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val message = "Read contact permission is needed to snap contact"
                    Snackbar.make(
                        activity?.findViewById(android.R.id.content) as View,
                        message,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("GRANT") {
                            requestPerm()
                        }.show()
                } else {
                    requestPerm()
                }
            }
        }
    }


    private fun initDialog() {
        if (!mAddTaskViewModel.namesCalendarsList.value.isNullOrEmpty()) {
            val dialog = CalendarSelectionDialogFragment(mAddTaskViewModel)
            dialog.show(childFragmentManager, "")
        } else
            Toast.makeText(requireContext(), "Your phone has no calendars!", Toast.LENGTH_LONG)
                .show()
    }


    private fun initSpinnerContacts() {
        mAddTaskViewModel.loadContacts()
        mAddTaskViewModel.listContacts.observe(viewLifecycleOwner, Observer {
            val contactsAdater =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            contactsAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.contactSpinner.adapter = contactsAdater
        })
    }

    private fun hasPermissionReadContacts(): Boolean {
        var result = 0
        result = checkCallingOrSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPerm() {
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance, android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.importanceSpinner.adapter = spinnerAdapter

        mBinding.importanceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!mIsInitSpinnerFinished) {
                        if (!mAddTaskViewModel.taskUI.importance.value.isNullOrEmpty())
                            parent?.setSelection(spinnerAdapter.getPosition(mAddTaskViewModel.taskUI.importance.value))
                        mIsInitSpinnerFinished = true
                    } else {
                        mAddTaskViewModel.taskUI.importance.value = parent?.selectedItem.toString()
                    }
                }
            }

    }


    private fun setDate() {
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mAddTaskViewModel.setDate(year, month, dayOfMonth)
            }
        DatePickerDialog(
            requireContext(), date, mAddTaskViewModel.currentYear,
            mAddTaskViewModel.currentMonth, mAddTaskViewModel.currentDay
        ).show()
    }

    private fun setTime(isTimeFrom: Boolean) {
        val time = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            mAddTaskViewModel.setTime(hourOfDay, minute, isTimeFrom)
        }
        TimePickerDialog(
            requireContext(), time, mAddTaskViewModel.currentHour,
            mAddTaskViewModel.currentMinute, true
        ).show()
    }

}