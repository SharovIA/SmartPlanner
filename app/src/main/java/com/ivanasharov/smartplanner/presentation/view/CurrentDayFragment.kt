package com.ivanasharov.smartplanner.presentation.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.CurrentDayViewModel
import kotlinx.android.synthetic.main.current_day_fragment.*
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.presentation.CurrentTasksAdapter
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskComponent


class CurrentDayFragment : Fragment() {
    private val component by lazy { AddTaskComponent.create()}

    private val currentDayViewModel by viewModels<CurrentDayViewModel>{ component.viewModelFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View? = inflater.inflate(R.layout.current_day_fragment, container, false)

        val tasksRecyclerView : RecyclerView? = view?.findViewById(R.id.currentDayRecyclerView)
        tasksRecyclerView?.layoutManager = LinearLayoutManager(activity)
        tasksRecyclerView?.setHasFixedSize(true)
        val arrayList = ArrayList<TaskUI>()
/*        var adapter = CurrentTasksAdapter(arrayList, requireContext(), {
            val status = currentDayViewModel.currentTasks.value?.get(it)?.status?.value
            if (status!=null && status)
                currentDayViewModel.currentTasks.value?.get(it)?.status?.value = false
            else currentDayViewModel.currentTasks.value?.get(it)?.status?.value =true

        })*/
        var adapter = CurrentTasksAdapter(arrayList, requireContext()) {
            currentDayViewModel.changeStatus(it)
        }
        tasksRecyclerView?.adapter = adapter

        currentDayViewModel.currentTasks.observe(this,  Observer<ArrayList<TaskUI>> {
            adapter.addTasksArrayList(it)
        })

        tasksRecyclerView?.adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObserve()

        addTaskForCurrentDayButton.setOnClickListener{
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

    private fun setObserve() {
        currentDayViewModel.date.observe(this, Observer{
            it?.let{
                currentDateTextView.text = currentDayViewModel.date.value
            }
        })

        currentDayViewModel.statusOfTasks.observe(this, Observer{
            it?.let{
                countTasksTextView.text = getString(R.string.completed) +" " + currentDayViewModel.statusOfTasks.value + " " + getString(
                                    R.string.completedTasks)
            }
        })
    }

}
