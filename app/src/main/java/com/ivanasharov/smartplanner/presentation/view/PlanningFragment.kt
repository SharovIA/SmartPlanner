package com.ivanasharov.smartplanner.presentation.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.PlanningFragmentBinding
import com.ivanasharov.smartplanner.databinding.TaskItemBinding
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.view.adapters.*
import com.ivanasharov.smartplanner.presentation.viewModel.PlanningViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanningFragment : Fragment() {

    private val mPlanningViewModel : PlanningViewModel by viewModels()
    private val mAdapter = UniversalListAdapter(
        HolderCreator(::createHolder),
        HolderBinder(::bindHolder),
        TaskDiffCallback()
    )

    private fun bindHolder(taskViewModel: TaskViewModel, holder: Holder<TaskItemBinding>) {
        holder.binding.viewModel = taskViewModel
        holder.binding.currentDayCheckBoxItem.setOnClickListener {
            mPlanningViewModel.changeStatus(holder.adapterPosition)
        }
        holder.binding.titleTextViewItem.setOnClickListener{
            findNavController().navigate(PlanningFragmentDirections.actionPlanningFragmentToShowTaskFragment(taskViewModel.id as Long))
        }

    }

    private fun createHolder(parent: ViewGroup): Holder<TaskItemBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  TaskItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    private lateinit var mBinding: PlanningFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.planning_fragment, container, false)
        mBinding.viewModel = mPlanningViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.currentDayRecyclerView.adapter = mAdapter
        mBinding.chooseDateTextView.setOnClickListener {
            setDate()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPlanningViewModel.taskList.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        mBinding.addTaskButton.setOnClickListener{
            findNavController().navigate(PlanningFragmentDirections.actionPlanningFragmentToAddTaskFragment(-1, getString(R.string.add_task)))
        }
    }

    private fun setDate() {
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mPlanningViewModel.setDate(year, month, dayOfMonth)
            }
        DatePickerDialog(
            requireContext(), date, mPlanningViewModel.currentYear,
            mPlanningViewModel.currentMonth, mPlanningViewModel.currentDay
        ).show()
    }
}
