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
//    private val currentDayViewModel: CurrentDayViewModel by viewModels()

    //private val taskViewModel by viewModels<CurrentDayViewModel>{ component.viewModelFactory()}
/*    companion object {
        fun newInstance() = CurrentDayFragment()
    }*/
  //  private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View? = inflater.inflate(R.layout.current_day_fragment, container, false)

        val tasksRecyclerView : RecyclerView? = view?.findViewById(R.id.currentDayRecyclerView)
        tasksRecyclerView?.layoutManager = LinearLayoutManager(activity)
        tasksRecyclerView?.setHasFixedSize(true)
        val arrayList = ArrayList<TaskUI>()
        var adapter = CurrentTasksAdapter(arrayList)
        tasksRecyclerView?.adapter = adapter

        currentDayViewModel.currentTasks.observe(this,  Observer<ArrayList<TaskUI>> {
            adapter.addTasksArrayList(it)
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
   //     viewModel = ViewModelProviders.of(this).get(CurrentDayViewModel::class.java)
        // TODO: Use the ViewModel
        setObserve()

        addTaskForCurrentDayButton.setOnClickListener{ Log.d("test", "HELLO!")
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

    private fun setObserve() {
        currentDayViewModel.date.observe(this, Observer{
            it?.let{
                currentDateTextView.text = currentDayViewModel.date.value
            }
        })
    }

}
