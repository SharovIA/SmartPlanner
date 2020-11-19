package com.ivanasharov.smartplanner.presentation.view

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.CurrentDayViewModel
import kotlinx.android.synthetic.main.current_day_fragment.*
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanasharov.smartplanner.presentation.CurrentTasksAdapter
import com.ivanasharov.smartplanner.presentation.Model.TaskUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentDayFragment : Fragment() {
//    private val component by lazy { AddTaskComponent.create()}

      private val currentDayViewModel : CurrentDayViewModel by viewModels()
//    private val currentDayViewModel by viewModels<CurrentDayViewModel>{ component.viewModelFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View? = inflater.inflate(R.layout.current_day_fragment, container, false)

        val tasksRecyclerView : RecyclerView? = view?.findViewById(R.id.currentDayRecyclerView)
        tasksRecyclerView?.layoutManager = LinearLayoutManager(activity)
        tasksRecyclerView?.setHasFixedSize(true)
        val arrayList = ArrayList<TaskUI>()

        var adapter = CurrentTasksAdapter(arrayList, requireContext()) {
            currentDayViewModel.changeStatus(it)
        }
        tasksRecyclerView?.adapter = adapter

        currentDayViewModel.currentTasks.observe(viewLifecycleOwner,  Observer<ArrayList<TaskUI>> {
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
        currentDayViewModel.date.observe(viewLifecycleOwner, Observer{
            it?.let{
                currentDateTextView.text = currentDayViewModel.date.value
            }
        })

        currentDayViewModel.statusOfTasks.observe(viewLifecycleOwner, Observer{
            it?.let{
                countTasksTextView.text = getString(R.string.completed) +" " + currentDayViewModel.statusOfTasks.value + " " + getString(
                                    R.string.completedTasks)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "CurrentDayFragment")
    }
}
