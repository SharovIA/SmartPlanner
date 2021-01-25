package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.CurrentDayViewModel
import kotlinx.android.synthetic.main.current_day_fragment.*
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.ivanasharov.smartplanner.data.IdNameStatus
import com.ivanasharov.smartplanner.databinding.CurrentDayFragmentBinding
import com.ivanasharov.smartplanner.databinding.TaskItemBinding
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.view.adapters.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentDayFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_CALENDAR = 11

    private val mCurrentDayViewModel : CurrentDayViewModel by viewModels()
    private val mAdapter = UniversalListAdapter(
        HolderCreator(::createHolder),
        HolderBinder(::bindHolder),
        TaskDiffCallback()
    )

    private fun bindHolder(viewModel: IdNameStatus, holder: Holder<TaskItemBinding>) {
        holder.binding.viewModel = viewModel
        holder.binding.mainViewModel = mCurrentDayViewModel
        holder.binding.currentDayCheckBoxItem.setOnClickListener {
            mCurrentDayViewModel.changeStatus(holder.adapterPosition)
        }
        holder.binding.titleTextViewItem.setOnClickListener{
            findNavController().navigate(CurrentDayFragmentDirections.actionCurrentDayFragmentToShowTaskFragment(viewModel.id))
        }

    }

    private fun createHolder(parent: ViewGroup): Holder<TaskItemBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  TaskItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    private lateinit var mBinding: CurrentDayFragmentBinding

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
            findNavController().navigate(CurrentDayFragmentDirections.actionCurrentDayFragmentToAddTaskFragment(-1, getString(R.string.add_task)))
               //findNavController().navigate(R.id.addTaskFragment)
        }

/*        mCurrentDayViewModel.statusOfTasks.observe(viewLifecycleOwner, Observer{
                mBinding.countTasksTextView.text = mCurrentDayViewModel.statusOfTasks.value
        })*/
    }

/*    private fun checkPermissionCalendar(): Boolean {
        // Проверка разрешения
        if (checkSelfPermission(requireContext(),
                Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED)
        {
            return true
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted")

            // Запрос разрешений
            Log.d(TAG, "Request permissions")

            val permissions: Array<String> = arrayOf( Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

            requestPermissions(permissions,
                PERMISSIONS_REQUEST_READ_CALENDAR)
        }
        return false
    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     //  setObserve()

/*        addTaskForCurrentDayButton.setOnClickListener{
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }*/
    }

/*   private fun setObserve() {
       mCurrentDayViewModel.date.observe(viewLifecycleOwner, Observer{
            it?.let{
                currentDateTextView.text = mCurrentDayViewModel.date.value
            }
        })

       mCurrentDayViewModel.statusOfTasks.observe(viewLifecycleOwner, Observer{
            it?.let{
                countTasksTextView.text = mCurrentDayViewModel.statusOfTasks.value
            }
        })
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "CurrentDayFragment")
    }
}
