package com.ivanasharov.smartplanner.presentation.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.presentation.viewModel.InformationViewModel

class InformationFragment : Fragment() {

    companion object {
        fun newInstance() = InformationFragment()
    }

    private lateinit var viewModel: InformationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.information_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(InformationViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "InformationFragment")
    }
}
