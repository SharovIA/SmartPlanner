package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskComponent
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*
import kotlin.collections.ArrayList


class AddTaskActivity : AppCompatActivity() {
    private val component by lazy {AddTaskComponent.create()}

    private val TAG = "CONTACT"
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 10

  //  private val taskViewModel by lazy {ViewModelProviders.of(this).get(AddTaskViewModel::class.java)}
    private val taskViewModel by viewModels<AddTaskViewModel>{ component.viewModelFactory()}
    private lateinit var spinnerAdapter :ArrayAdapter<CharSequence>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        setObserve()

        initSpinner()
        initListeners()

    }

    private fun initListeners() {
        dateTextViewATActivity.setOnClickListener{setDate()}
        time1TextViewATActivity.setOnClickListener{setTime(true)}
        time2TextViewATActivity.setOnClickListener{setTime(false)}
        saveButton.setOnClickListener {
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

    private fun getDataFromActivity(){
        taskViewModel.taskUILiveData.name.value = nameEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.description.value = descriptionEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.address.value = addressEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.importance.value = importanceSpinner.selectedItem.toString()
        taskViewModel.taskUILiveData.isAddToCalendar.value = addCalendarAndroidCheckBox.isChecked
        taskViewModel.taskUILiveData.isSnapContact.value = addContactCheckBox.isChecked
        if (addContactCheckBox.isChecked)
            taskViewModel.taskUILiveData.contact.value = contactSpinner.selectedItem.toString()
    }

    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(this,
            R.array.importance, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        importanceSpinner.adapter = spinnerAdapter
    }

    private fun setObserve() {
        taskViewModel.taskUILiveData.date.observe(this, Observer{
            it?.let{
               // dateTextViewATActivity.text = taskViewModel.taskUILiveData.date.value
                dateTextViewATActivity.text = taskViewModel.getFullDate()
            }
        })

        taskViewModel.taskUILiveData.timeFrom.observe(this, Observer{
            it?.let{
                time1TextViewATActivity.text = taskViewModel.taskUILiveData.timeFrom.value
            }
        })

        taskViewModel.taskUILiveData.timeTo.observe(this, Observer{
            it?.let{
                time2TextViewATActivity.text = taskViewModel.taskUILiveData.timeTo.value
            }
        })
}


    private fun setDate(){
        val calendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                taskViewModel.day = dayOfMonth
                taskViewModel.month = month+1
                taskViewModel.year = year
                taskViewModel.updateFullDate()
            }
        DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setTime(firstTime : Boolean){
        val calendar = Calendar.getInstance()
        val time = TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
            if(firstTime) {
                taskViewModel.hours1 = hourOfDay
                taskViewModel.minutes1 = minute
                taskViewModel.updateFullTimeFrom()
            }
            else{
                taskViewModel.hours2 = hourOfDay
                taskViewModel.minutes2 = minute
                taskViewModel.updateFullTimeTo()
            }
        }
        TimePickerDialog(this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
}

