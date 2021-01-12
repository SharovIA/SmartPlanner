package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.FragmentAddTaskBinding
import com.ivanasharov.smartplanner.presentation.view.dialogs.CalendarSelectionDialogFragment
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
//class AddTaskFragment : FragmentActivity() {
class AddTaskFragment : Fragment() {

    companion object{
        private const val TAG = "CONTACT"
        private const val PERMISSIONS_REQUEST_CODE = 123
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 10
        private const val PERMISSIONS_REQUEST_READ_CALENDAR = 11
        private const val PERMISSIONS_REQUEST_WRITE_CALENDAR = 12
    }

    private var mIsPermissionForReadCalendar = false
    private var mIsPermissionForWriteToCalendar = false

    private val mAddTaskViewModel : AddTaskViewModel by viewModels()
    private val mArguments: AddTaskFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentAddTaskBinding
    private lateinit var spinnerAdapter : ArrayAdapter<CharSequence>
    private lateinit var mContext : Context

    private var mIsInitSpinnerFinished  = false
  //  private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
/*        val bundle = arguments
        if(bundle != null) {
            mAddTaskViewModel.loadTaskForEdit(bundle.getLong("id"))
            mAddTaskViewModel.modeNewTask = false       //Edit mode
        }*/
        if (mArguments.idOfTask != -1L){
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
        mContext = inflater.context
        initSpinner()
        initListeners()



        // Inflate the layout for this fragment
/*        if (!mAddTaskViewModel.modeNewTask) {
            val toolbar: Toolbar =
                activity?.findViewById<Toolbar>(R.id.toolbar_actionbar) as Toolbar
            toolbar.title = getString(R.string.edit_task)
        }*/
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

        mBinding.addCalendarAndroidCheckBox.setOnClickListener{
            if(hasPermissionReadCalendar()) {
                chooseCalendar()

            } else
                requestPermissionWithRationale()
        }

        mBinding.saveButton.setOnClickListener {
            getDataFromActivity()
            if (mAddTaskViewModel.isAddToCalendar.value != null && mAddTaskViewModel.isAddToCalendar.value as Boolean){
                // Проверка разрешения
                if (mIsPermissionForWriteToCalendar)
                    mAddTaskViewModel.save()
                else
                    checkCalendarPermissionWrite()

            }
            else
                mAddTaskViewModel.save()
        }

        mBinding.addContactCheckBox.setOnClickListener {
            getListOfContacts()
        }
/*
        mBinding.addCalendarAndroidCheckBox.setOnClickListener {
            initDialog()
        }*/
    }

    private fun chooseCalendar() {
        mAddTaskViewModel.loadCalendars()
        mAddTaskViewModel.namesCalendarsList.observe(viewLifecycleOwner, Observer{
            initDialog()
        })

    }

    private fun hasPermissionReadCalendar(): Boolean {
        var result = 0
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        for (permission in permissions){
            result = checkCallingOrSelfPermission(requireContext(), permission)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun requestPerms(){
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var allowed = true

        when(requestCode){
            PERMISSIONS_REQUEST_CODE -> {
                for(res in grantResults) {
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED)
                }
            }
            else -> {
                // if user not granted permissions.
                allowed = false
            }
        }

        if(allowed){
            chooseCalendar()
        } else{
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR)){
                    Toast.makeText(requireContext(), "Calendar Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoCalendarPermissionSnackbar();
                }
            }
        }
    }

    private fun showNoCalendarPermissionSnackbar() {
        Snackbar.make(activity?.findViewById(R.id.content) as View,"", Snackbar.LENGTH_LONG)
            .setAction("Settings", View.OnClickListener{
                openApplicationSettings()
                Toast.makeText(requireContext(), "Open Permissions and grant the read and write permissions",
                    Toast.LENGTH_SHORT)
                    .show()
            })
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + activity?.packageName))
        startActivityForResult(appSettingsIntent, PERMISSIONS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSIONS_REQUEST_CODE){
            chooseCalendar()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestPermissionWithRationale() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
            Manifest.permission.READ_CALENDAR)){
            val message = "Storage permission is needed to show files count"
            Snackbar.make(
                activity?.findViewById(R.id.content) as View,
                message,
                Snackbar.LENGTH_LONG
            ).setAction("GRANT") {
                requestPerms()
            }.show()
        } else{
            requestPerms()
        }
    }
//----------------------------
    private fun checkCalendarPermissionRead() {
        // Проверка разрешения
        if (checkSelfPermission(requireContext(),
                Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED)
        {
            mIsPermissionForReadCalendar = true

            Log.d(TAG, "ok")
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted")

            // Запрос разрешений
            Log.d(TAG, "Request permissions")

            val permissions: Array<String> = arrayOf( Manifest.permission.READ_CALENDAR)

            requestPermissions(permissions,
                PERMISSIONS_REQUEST_READ_CALENDAR)

        }
    }

    private fun checkCalendarPermissionWrite() {
        // Проверка разрешения
        if (checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED)
        {
            mIsPermissionForWriteToCalendar = true

            Log.d(TAG, "ok")
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted")

            // Запрос разрешений
            Log.d(TAG, "Request permissions")

            val permissions: Array<String> = arrayOf(Manifest.permission.WRITE_CALENDAR)

            requestPermissions(permissions,
                PERMISSIONS_REQUEST_WRITE_CALENDAR)

        }
    }
//----------------------------------------------------------------------------------
/*    private fun initListeners() {
        mBinding.dateTextView.setOnClickListener{
            setDate()
        }
        mBinding.time1TextView.setOnClickListener{setTime(true)}
        mBinding.time2TextView.setOnClickListener{setTime(false)}
        mBinding.saveButton.setOnClickListener {
            getDataFromActivity()
            if (mAddTaskViewModel.isAddToCalendar.value != null && mAddTaskViewModel.isAddToCalendar.value as Boolean){
                // Проверка разрешения
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(requireContext(),
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
    }*/

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
      //  mAddTaskViewModel.taskUI.importance.value = mBinding.importanceSpinner.selectedItem.toString()
        if (mBinding.addContactCheckBox.isChecked){
            mAddTaskViewModel.taskUI.contact.value = mBinding.contactSpinner.selectedItem.toString()
        }
    }

/*    private fun initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.importance, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.importanceSpinner.adapter = spinnerAdapter
    }*/
private fun initSpinner() {
    spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
        R.array.importance, android.R.layout.simple_spinner_item)
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mBinding.importanceSpinner.adapter = spinnerAdapter

    mBinding.importanceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
/*            if ((mAddTaskViewModel.taskUI.importance.value != null) &&  (mAddTaskViewModel.taskUI.importance.value?.isNotEmpty() as Boolean))

            else
                mAddTaskViewModel.taskUI.importance.value = parent?.selectedItem.toString()*/
            if(!mIsInitSpinnerFinished){//init
                if (!mAddTaskViewModel.taskUI.importance.value.isNullOrEmpty() )
                    parent?.setSelection(spinnerAdapter.getPosition(mAddTaskViewModel.taskUI.importance.value))
                mIsInitSpinnerFinished = true
            } else{//choose
                mAddTaskViewModel.taskUI.importance.value = parent?.selectedItem.toString()
            }
/*            val oldValueOfImportanceInVM =

                isFirst = false
            } else {
                val selectionValue = parent?.selectedItem.toString()
                if (selectionValue =="")
                    parent?.setSelection(spinnerAdapter.getPosition(mAddTaskViewModel.taskUI.importance.value))
                isFirst = false
                else {

                }
                val newValueOfImportance = parent?.selectedItem.toString()
                if (newValueOfImportance != oldValueOfImportance){
                    mAddTaskViewModel.taskUI.importance.value = newValueOfImportance
                }*/

            }
/*            mAddTaskViewModel.taskUI.importance.observe(viewLifecycleOwner, Observer{

            })*/

        }

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