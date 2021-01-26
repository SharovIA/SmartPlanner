package com.ivanasharov.smartplanner.presentation.view.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.CurrentDayViewModel
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ivanasharov.smartplanner.data.database.requests_model.IdNameStatus
import com.ivanasharov.smartplanner.databinding.CurrentDayFragmentBinding
import com.ivanasharov.smartplanner.databinding.TaskItemBinding
import com.ivanasharov.smartplanner.presentation.view.adapters.diffcallback.TaskDiffCallback
import com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews.universal.Holder
import com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews.universal.HolderBinder
import com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews.universal.HolderCreator
import com.ivanasharov.smartplanner.presentation.view.adapters.recyclerviews.universal.UniversalListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentDayFragment : Fragment() {

    private val mCurrentDayViewModel : CurrentDayViewModel by viewModels()
    private lateinit var mBinding: CurrentDayFragmentBinding
    private val mAdapter =
        UniversalListAdapter(HolderCreator(::createHolder), HolderBinder(::bindHolder),
            TaskDiffCallback()
        )

    private fun bindHolder(viewModel: IdNameStatus, holder: Holder<TaskItemBinding>) {
        holder.binding.viewModel = viewModel
        holder.binding.mainViewModel = mCurrentDayViewModel
        holder.binding.currentDayCheckBoxItem.setOnClickListener {
            mCurrentDayViewModel.changeStatus(holder.adapterPosition)
        }
        holder.binding.titleTextViewItem.setOnClickListener{
            findNavController().navigate(
                CurrentDayFragmentDirections.actionCurrentDayFragmentToShowTaskFragment(
                    viewModel.id
                )
            )
        }
    }

    private fun createHolder(parent: ViewGroup): Holder<TaskItemBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  TaskItemBinding.inflate(inflater, parent, false)
        return Holder(
            binding
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.current_day_fragment, container, false)
        mBinding.viewModel = mCurrentDayViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.currentDayRecyclerView.adapter = mAdapter
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCurrentDayViewModel.taskList.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        mBinding.addTaskForCurrentDayButton.setOnClickListener{
            findNavController().navigate(
                CurrentDayFragmentDirections.actionCurrentDayFragmentToAddTaskFragment(
                    -1,
                    getString(R.string.add_task)
                )
            )
        }
    }

}
