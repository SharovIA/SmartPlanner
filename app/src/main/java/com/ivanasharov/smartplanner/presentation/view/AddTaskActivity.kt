package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.ActivityAddTaskBinding
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {


    private val TAG = "CONTACT"
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 10

    private val taskViewModel : AddTaskViewModel by viewModels()
   private lateinit var mBinding: ActivityAddTaskBinding
    private lateinit var spinnerAdapter :ArrayAdapter<CharSequence>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_task)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = taskViewModel
        initSpinner()
        initListeners()

    }

    private fun initListeners() {
        mBinding.dateTextViewATActivity.setOnClickListener{setDate()}
        mBinding.time1TextViewATActivity.setOnClickListener{setTime(true)}
        mBinding.time2TextViewATActivity.setOnClickListener{setTime(false)}
        mBinding.saveButton.setOnClickListener {
                 getDataFromActivity()
                 taskViewModel.save()
                 finish()
             }

        addContactCheckBox.setOnClickListener {
            getListOfContacts()
        }
    }

    private fun getListOfContacts() {
        // Проверка разрешения
        if (ContextCompat.checkSelfPermission(this,
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
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfItems)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Set Adapter to Spinner
            contactSpinner!!.adapter = aa


            Log.d(TAG, "ok")
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted")

            // Запрос разрешений
            Log.d(TAG, "Request permissions")

            val permissions: Array<String> = arrayOf( Manifest.permission.READ_CONTACTS)

            ActivityCompat.requestPermissions(this, permissions,
                PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    private fun readContacts() : ArrayList<Contact> {
        val listContacts = ArrayList<Contact>()
        val cursor = contentResolver.query(
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
                   val pCur = contentResolver.query(
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
    taskViewModel.taskUI.importance.value = mBinding.importanceSpinner.selectedItem.toString()
    if (mBinding.addContactCheckBox.isChecked){
        taskViewModel.taskUI.contact.value = mBinding.contactSpinner.selectedItem.toString()
    }
    }

    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(this,
            R.array.importance, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.importanceSpinner.adapter = spinnerAdapter
    }

    private fun setDate(){
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                taskViewModel.setDate(year, month, dayOfMonth)
            }
        DatePickerDialog(this, date, taskViewModel.currentYear,
            taskViewModel.currentMonth, taskViewModel.currentDay).show()
    }

     private fun setTime(isTimeFrom : Boolean){
        val time = TimePickerDialog.OnTimeSetListener{ _, hourOfDay, minute ->
                taskViewModel.setTime(hourOfDay, minute, isTimeFrom)
        }
        TimePickerDialog(this, time, taskViewModel.currentHour,
            taskViewModel.currentMinute, true).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("run", "AddTaskActivity")
    }
}

