package com.ivanasharov.smartplanner.presentation.view.fragments

import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.ShowTaskFragmentBinding
import com.ivanasharov.smartplanner.presentation.viewModel.ShowTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class ShowTaskFragment : Fragment(), OnMapReadyCallback {

    companion object{
        private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    private val mShowTaskViewModel : ShowTaskViewModel by viewModels()
    private lateinit var mBinding: ShowTaskFragmentBinding
    private val mArguments: ShowTaskFragmentArgs by navArgs()
    private var mIsCorrectAddress = false
    private var mQmap : GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.show_task_fragment, container, false)
        mShowTaskViewModel.loadTask(mArguments.idOfTask)
        mBinding.viewModel = mShowTaskViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
        val toolbar : Toolbar  = activity?.findViewById<Toolbar>(R.id.toolbar_actionbar) as Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

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

    private fun showMap() {
        if(mBinding.mapView.visibility == View.GONE){
            onMapReady(mQmap)
            if (mIsCorrectAddress) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_task_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.editInShowTaskMenu && mShowTaskViewModel.task.value?.id != null) {
            findNavController().navigate(
                ShowTaskFragmentDirections.actionShowTaskFragmentToAddTaskFragment(
                    mShowTaskViewModel.task.value?.id as Long,
                    getString(R.string.edit_task)
                )
            )
        }
        return super.onOptionsItemSelected(item)
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
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mQmap = p0
        mQmap?.setMinZoomPreference(12f)
        mQmap?.isIndoorEnabled = true
        val uiSettings = mQmap?.uiSettings
        uiSettings?.isIndoorLevelPickerEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        uiSettings?.isMapToolbarEnabled = true
        uiSettings?.isCompassEnabled = true
        uiSettings?.isZoomControlsEnabled = true
        val  strAddress = mShowTaskViewModel.task.value?.address

        if (strAddress != null) {
            val coordinates = getCoordinaty(strAddress)
            if (coordinates != null) {
                mIsCorrectAddress = true
                mQmap?.addMarker(MarkerOptions().position(coordinates))
                mQmap?.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }
    }

    fun getCoordinaty(strAddress: String) : LatLng? {
        val loc = Locale(Locale.getDefault().language)
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