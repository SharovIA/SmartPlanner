package com.ivanasharov.smartplanner.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.DailyScheduleFragmentBinding
import com.ivanasharov.smartplanner.presentation.DayRecyclerViewAdapter
import com.ivanasharov.smartplanner.presentation.viewModel.DailyScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DailyScheduleFragment : Fragment() {

    private val mDailyScheduleViewModel : DailyScheduleViewModel by viewModels()

    private lateinit var mDayRecyclerViewAdapter : DayRecyclerViewAdapter
    private lateinit var mBinding: DailyScheduleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.daily_schedule_fragment, container, false)
        mBinding.viewModel= mDailyScheduleViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        return mBinding.root

    }

    private fun initRecyclerView() {
        mBinding.scheduleRecyclerView.apply {
            mDayRecyclerViewAdapter = DayRecyclerViewAdapter()
            adapter = mDayRecyclerViewAdapter
            this.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
            this.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDailyScheduleViewModel.listTasks.observe(viewLifecycleOwner, Observer{
            mDayRecyclerViewAdapter.submitList(it)
        })

    }

}
