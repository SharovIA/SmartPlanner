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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ivanasharov.smartplanner.databinding.CurrentDayFragmentBinding
import com.ivanasharov.smartplanner.databinding.TaskItemBinding
import com.ivanasharov.smartplanner.presentation.model.TaskViewModel
import com.ivanasharov.smartplanner.presentation.view.adapters.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentDayFragment : Fragment() {

    private val mCurrentDayViewModel : CurrentDayViewModel by viewModels()
    private val mAdapter = UniversalListAdapter(
        HolderCreator(::createHolder),
        HolderBinder(::bindHolder),
        TaskDiffCallback()
    )

    private fun bindHolder(taskViewModel: TaskViewModel, holder: Holder<TaskItemBinding>) {
        holder.binding.viewModel = taskViewModel
        holder.binding.mainViewModel = mCurrentDayViewModel
        holder.binding.currentDayCheckBoxItem.setOnClickListener {
            mCurrentDayViewModel.changeStatus(holder.adapterPosition)
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



/*        val view : View? = inflater.inflate(R.layout.current_day_fragment, container, false)

       val tasksRecyclerView : RecyclerView? = view?.findViewById(R.id.currentDayRecyclerView)
        tasksRecyclerView?.layoutManager = LinearLayoutManager(activity)
        tasksRecyclerView?.setHasFixedSize(true)
        val arrayList = ArrayList<TaskUI>()

        var adapter = CurrentTasksAdapter(arrayList, requireContext()) {
            mCurrentDayViewModel.changeStatus(it)
        }
        tasksRecyclerView?.adapter = adapter

        mCurrentDayViewModel.currentTasks.observe(viewLifecycleOwner,  Observer<ArrayList<TaskUI>> {
            adapter.addTasksArrayList(it)
        })

        tasksRecyclerView?.adapter
        return view*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCurrentDayViewModel.taskList.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })

/*        mCurrentDayViewModel.statusOfTasks.observe(viewLifecycleOwner, Observer{
                mBinding.countTasksTextView.text = mCurrentDayViewModel.statusOfTasks.value
        })*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     //  setObserve()

        addTaskForCurrentDayButton.setOnClickListener{
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
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
