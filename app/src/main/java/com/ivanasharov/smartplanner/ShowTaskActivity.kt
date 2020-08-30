package com.ivanasharov.smartplanner

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskComponent
import com.ivanasharov.smartplanner.presentation.viewModel.ShowTaskViewModel
import kotlinx.android.synthetic.main.activity_show_task.*
import java.util.*

class ShowTaskActivity : AppCompatActivity() {

    private val component by lazy { AddTaskComponent.create()}

    private val showTaskViewModel by viewModels<ShowTaskViewModel>{ component.viewModelFactory()}

    lateinit var observerName: Observer<String?>
    lateinit var observerDescription: Observer<String?>
    lateinit var observerDate: Observer<GregorianCalendar?>
    lateinit var observerTimeFrom: Observer<String?>
    lateinit var observerTimeTo: Observer<String?>
    lateinit var observerImportance: Observer<String?>
    lateinit var observerAddress: Observer<String?>
    lateinit var observerContact: Observer<String?>
    lateinit var observerStatus: Observer<Boolean?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_task)

        initObserve()
        val intent = intent
        if (intent != null) { showTaskViewModel.init(intent) }


      //  setListeners()
    }



    private fun initObserve() {
        observerName = Observer{
            it?.let{
                nameTextViewShowActivity.text = it
            }
        }
        observerDescription = Observer{
            it?.let{
                descriptionTextViewShowActivity.text = it
            }
        }

        observerDate = Observer{
            it?.let{
                dateTextViewShowActivity.text = "${it.get(GregorianCalendar.DAY_OF_MONTH)}-" +
                        "${it.get(GregorianCalendar.MONTH)}-" +
                        "${it.get(GregorianCalendar.YEAR)}"
            }
        }

        observerTimeFrom = Observer{
            it?.let{
                timeTextViewShowActivity.text = it +" - "+showTaskViewModel.taskUI.timeTo
            }
        }

        observerTimeTo = Observer{
            it?.let{
                timeTextViewShowActivity.text = showTaskViewModel.taskUI.timeFrom.value +" - " + it
            }
        }

        observerImportance = Observer{
            it?.let{
                importanceTextViewShowActivity.text = it
            }
        }

        observerAddress = Observer{
            it?.let{
                addressTextViewShowActivity.text = it
            }
        }

        observerContact = Observer{
            it?.let{
                snapContactTextViewShowActivity.text = getString(R.string.tied_contact) + it
            }
        }

        observerStatus = Observer{
            it?.let{
                if(it){
                    statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                    statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                }
                else{
                    statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                    statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                }
            }
        }
    }

 /*   private fun setListeners() {
        var status : Boolean
        statusTrueTextViewShowActivity.setOnClickListener {
           status =  showTaskViewModel.taskUI.status.value as Boolean
            if (!status){
                statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                showTaskViewModel.taskUI.status.value = true

            }
        }
        statusFalseTextViewShowActivity.setOnClickListener {
            status =  showTaskViewModel.taskUI.status.value as Boolean
            if (status){
                statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                showTaskViewModel.taskUI.status.value = false
            }
        }
    }*/

/*    private fun initData(intent: Intent) {
        showTaskViewModel.taskUI.name.value = intent.getStringExtra("name")
        showTaskViewModel.taskUI.description.value = intent.getStringExtra("description")
        showTaskViewModel.taskUI.date.value = showTaskViewModel.longToCalendar(intent.getLongExtra("date", 0))
        showTaskViewModel.taskUI.timeFrom.value = intent.getStringExtra("timeFrom")
        showTaskViewModel.taskUI.timeTo.value = intent.getStringExtra("timeTo")
        showTaskViewModel.taskUI.importance.value = intent.getStringExtra("importance")
        showTaskViewModel.taskUI.address.value = intent.getStringExtra("address")
        showTaskViewModel.taskUI.isShowMap.value = intent.getBooleanExtra("isShowMap", false)
        showTaskViewModel.taskUI.isSnapContact.value = intent.getBooleanExtra("isSnapContact", false)
        showTaskViewModel.taskUI.contact.value = intent.getStringExtra("contact")
        showTaskViewModel.taskUI.status.value = intent.getBooleanExtra("status", false)
    }*/

    override fun onStart() {
        super.onStart()
        showTaskViewModel.taskUI.name.observe(this, observerName)
        showTaskViewModel.taskUI.description.observe(this, observerDescription)
        showTaskViewModel.taskUI.date.observe(this, observerDate)
        showTaskViewModel.taskUI.timeFrom.observe(this, observerTimeFrom)
        showTaskViewModel.taskUI.timeTo.observe(this, observerTimeTo)
        showTaskViewModel.taskUI.importance.observe(this, observerImportance)
        showTaskViewModel.taskUI.address.observe(this, observerAddress)
        showTaskViewModel.taskUI.contact.observe(this, observerContact)
        showTaskViewModel.taskUI.status.observe(this, observerStatus)
    }

    override fun onStop() {
        super.onStop()
        showTaskViewModel.taskUI.name.removeObserver(observerName)
        showTaskViewModel.taskUI.description.removeObserver(observerDescription)
        showTaskViewModel.taskUI.date.removeObserver(observerDate)
        showTaskViewModel.taskUI.timeFrom.removeObserver(observerTimeFrom)
        showTaskViewModel.taskUI.timeTo.removeObserver(observerTimeTo)
        showTaskViewModel.taskUI.importance.removeObserver(observerImportance)
        showTaskViewModel.taskUI.address.removeObserver(observerAddress)
        showTaskViewModel.taskUI.contact.removeObserver(observerContact)
        showTaskViewModel.taskUI.status.removeObserver(observerStatus)
    }
}
