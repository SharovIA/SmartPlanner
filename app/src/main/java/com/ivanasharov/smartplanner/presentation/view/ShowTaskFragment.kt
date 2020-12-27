package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.ContactsContract
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivanasharov.smartplanner.Contact
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.FragmentAddTaskBinding
import com.ivanasharov.smartplanner.databinding.ShowTaskFragmentBinding
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskViewModel
import com.ivanasharov.smartplanner.presentation.viewModel.ShowTaskViewModel
import java.io.IOException
import java.util.*

class ShowTaskFragment : Fragment(), OnMapReadyCallback {

    private val mShowTaskViewModel : ShowTaskViewModel by viewModels()
    private lateinit var mBinding: ShowTaskFragmentBinding
    private val mArguments: ShowTaskFragmentArgs by navArgs()
    private var isCorrectAddress = false

    private var  gmap : GoogleMap? = null

    companion object{
        private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.show_task_fragment, container, false)
        mShowTaskViewModel.time = mArguments.taskViewModel.timeFrom + " - "+ mArguments.taskViewModel.timeTo
        mBinding.mainViewModel = mShowTaskViewModel
        mBinding.viewModel = mArguments.taskViewModel //получаем аргументы через навигацию
        mBinding.lifecycleOwner = viewLifecycleOwner

        return mBinding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mapViewBundle : Bundle? = null
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)

        mBinding.mapView.onCreate(mapViewBundle)
        mBinding.mapView.getMapAsync(this)

        mBinding.showMapLabel.setOnClickListener {
            showMap()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "destroy AddTaskFragment")

    }


    private fun showMap() {
        if(mBinding.mapView.visibility == View.GONE){
            onMapReady(gmap)
            if (isCorrectAddress) {
                mBinding.mapView.visibility = View.VISIBLE
                mBinding.showMapLabel.text = getString(R.string.hide_map)
            } else {
                Toast.makeText(requireContext(), "Address is incorrect!", Toast.LENGTH_LONG).show()
            }

        } else {
            mBinding. mapView.visibility = View.GONE
            mBinding.showMapLabel.text = getString(R.string.showMap)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle : Bundle? = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle =  Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mBinding.mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
        Log.d("run", "ShowTaskFragment")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }


/*    override fun onMapReady(map: GoogleMap?) {
        showTaskViewModel.onMapReady()
    }*/

    override fun onMapReady(p0: GoogleMap?) {
        gmap = p0
        gmap?.setMinZoomPreference(12f)
        gmap?.isIndoorEnabled = true
        val uiSettings = gmap?.uiSettings
        uiSettings?.isIndoorLevelPickerEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isMapToolbarEnabled = true
        uiSettings?.isCompassEnabled = true
        uiSettings?.isZoomControlsEnabled = true
        val  strAddress = mBinding.viewModel?.address

        if (strAddress != null) {
            val coordinates = getCoordinaty(strAddress)
            if (coordinates != null) {
                isCorrectAddress = true
                gmap?.addMarker(MarkerOptions().position(coordinates))
                gmap?.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }

    fun getCoordinaty(strAddress: String) : LatLng? {
        val loc = Locale("RU")
        val coder = Geocoder(requireContext(), loc)
        var lat: Double
        var lon: Double

        coder.apply {
            try {
                getFromLocationName(strAddress, 1).let {
                    if (it.isNotEmpty()) {
                        lat = it[0].latitude
                        lon = it[0].longitude
                        val latLng = LatLng(lat, lon)
                        return latLng
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }


    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()

    }
}