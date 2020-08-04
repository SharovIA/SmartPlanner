package com.ivanasharov.smartplanner.presentation.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.ivanasharov.smartplanner.DI
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskComponent
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val component by lazy {AddTaskComponent.create()}

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
    }

    private fun getDataFromActivity(){
        taskViewModel.taskUILiveData.name.value = nameEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.description.value = descriptionEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.address.value = addressEditTextATActivity.text.toString()
        taskViewModel.taskUILiveData.importance.value = importanceSpinner.selectedItem.toString()
        taskViewModel.taskUILiveData.isShowMap.value = showMapCheckBox.isChecked
        taskViewModel.taskUILiveData.isAddToCalendar.value = addCalendarAndroidCheckBox.isChecked
        taskViewModel.taskUILiveData.isSnapContact.value = addContactCheckBox.isChecked
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

