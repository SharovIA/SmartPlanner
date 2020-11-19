package com.ivanasharov.smartplanner.presentation.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.FreeTimeViewModel

class FreeTimeFragment : Fragment() {

    companion object {
        fun newInstance() = FreeTimeFragment()
    }

    private lateinit var viewModel: FreeTimeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.free_time_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FreeTimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "FreeTimeFragment")
    }

}
