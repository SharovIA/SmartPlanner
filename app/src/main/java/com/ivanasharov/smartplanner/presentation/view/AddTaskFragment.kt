package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.ActivityAddTaskBinding
import com.ivanasharov.smartplanner.databinding.FragmentAddTaskBinding
import com.ivanasharov.smartplanner.presentation.view.dialogs.CalendarSelectionDialogFragment
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*

@AndroidEntryPoint
//class AddTaskFragment : FragmentActivity() {
class AddTaskFragment : Fragment() {

    private val TAG = "CONTACT"
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 10
    private val PERMISSIONS_REQUEST_READ_CALENDAR = 11
    private val PERMISSIONS_REQUEST_WRITE_CALENDAR = 12

    private val mAddTaskViewModel : AddTaskViewModel by viewModels()
    private lateinit var mBinding: FragmentAddTaskBinding
    private lateinit var spinnerAdapter : ArrayAdapter<CharSequence>
    private lateinit var mContext : Context
  //  private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mAddTaskViewModel
        mContext = inflater.context
        initSpinner()
        initListeners()
        // Inflate the layout for this fragment
        return mBinding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTaskViewModel.isSave.observe(viewLifecycleOwner, Observer{
            if (it){
                findNavController().popBackStack()
            } else{
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initListeners() {
        mBinding.dateTextView.setOnClickListener{
            setDate()
        }
        mBinding.time1TextView.setOnClickListener{setTime(true)}
        mBinding.time2TextView.setOnClickListener{setTime(false)}
        mBinding.saveButton.setOnClickListener {
            getDataFromActivity()
            if (mAddTaskViewModel.isAddToCalendar.value != null && mAddTaskViewModel.isAddToCalendar.value as Boolean){
                // Проверка разрешения
                if (checkSelfPermission(requireContext(),
                        Manifest.permission.READ_CALENDAR) ==
                    PackageManager.PERMISSION_GRANTED && checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_CALENDAR) ==
                    PackageManager.PERMISSION_GRANTED)
                {
                    mAddTaskViewModel.save()

                    Log.d(TAG, "ok")
                } else {
                    // Разрешений нет
                    Log.d(TAG, "Permission is not granted")

                    // Запрос разрешений
                    Log.d(TAG, "Request permissions")

                    val permissions: Array<String> = arrayOf( Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

                    requestPermissions(permissions,
                        PERMISSIONS_REQUEST_WRITE_CALENDAR)
                }
            }
            else
                mAddTaskViewModel.save()
        }

        mBinding.addContactCheckBox.setOnClickListener {
            getListOfContacts()
        }

        mBinding.addCalendarAndroidCheckBox.setOnClickListener {
            initDialog()
        }
    }

    private fun initDialog() {
        if (!mAddTaskViewModel.namesCalendarsList.value.isNullOrEmpty()) {
            val dialog = CalendarSelectionDialogFragment(mAddTaskViewModel)
            dialog.show(childFragmentManager, "")
        } else
            Toast.makeText(requireContext(), "Your phone has no calendars!", Toast.LENGTH_LONG).show()
    }

    private fun getListOfContacts() {
        // Проверка разрешения
        if (checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED)
        {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted")
            val listContacts = readContacts()

            val listOfItems = ArrayList<String>()
            for (contact in listContacts){
                listOfItems.add(contact.getData())
            }
            // Create an ArrayAdapter using a simple spinner layout and languages array
            val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOfItems)
            // Set layout to use when requireContext()the list of choices appear
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Set Adapter to Spinner
            mBinding.contactSpinner.adapter = aa


            Log.d(TAG, "ok")
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted")

            // Запрос разрешений
            Log.d(TAG, "Request permissions")

            val permissions: Array<String> = arrayOf( Manifest.permission.READ_CONTACTS)

            requestPermissions(permissions,
                PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    private fun readContacts() : ArrayList<Contact> {
        val listContacts = ArrayList<Contact>()
        val cursor = activity?.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);

        if(cursor != null && cursor.count > 0) {
            while(cursor.moveToNext()) {
                val id = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts._ID))
                val name = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts
                            .DISPLAY_NAME))
                val has_phone = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts
                            .HAS_PHONE_NUMBER))
                var phone = ""
                if (Integer.parseInt(has_phone) > 0) {
                    // extract phone number
                    val args: Array<String> = arrayOf(id)
                    val pCur = activity?.contentResolver?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        args, null)

                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            phone = pCur.getString (pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                        pCur.close()
                    }
                }

                val contact = Contact(id, name, phone)
                listContacts.add(contact)
            }
        }
        return listContacts
    }
    //---------------------------------------
    private fun getDataFromActivity(){
        mAddTaskViewModel.taskUI.importance.value = mBinding.importanceSpinner.selectedItem.toString()
        if (mBinding.addContactCheckBox.isChecked){
            mAddTaskViewModel.taskUI.contact.value = mBinding.contactSpinner.selectedItem.toString()
        }
    }

    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.importance, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.importanceSpinner.adapter = spinnerAdapter
    }

    private fun setDate(){
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mAddTaskViewModel.setDate(year, month, dayOfMonth)
            }
        DatePickerDialog(requireContext(), date, mAddTaskViewModel.currentYear,
            mAddTaskViewModel.currentMonth, mAddTaskViewModel.currentDay).show()
    }

    private fun setTime(isTimeFrom : Boolean){
        val time = TimePickerDialog.OnTimeSetListener{ _, hourOfDay, minute ->
            mAddTaskViewModel.setTime(hourOfDay, minute, isTimeFrom)
        }
        TimePickerDialog(requireContext(), time, mAddTaskViewModel.currentHour,
            mAddTaskViewModel.currentMinute, true).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "destroy AddTaskFragment")

    }
}