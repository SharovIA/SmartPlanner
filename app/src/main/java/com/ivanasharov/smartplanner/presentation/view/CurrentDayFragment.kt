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



class CurrentDayFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentDayFragment()
    }

    private lateinit var viewModel: CurrentDayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_day_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentDayViewModel::class.java)
        // TODO: Use the ViewModel
        addTaskForCurrentDayButton.setOnClickListener{ Log.d("test", "HELLO!")
            startActivity(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

}
