package com.ivanasharov.smartplanner

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivanasharov.smartplanner.presentation.viewModel.AddTaskComponent
import com.ivanasharov.smartplanner.presentation.viewModel.ShowTaskViewModel
import kotlinx.android.synthetic.main.activity_show_task.*
import java.io.IOException
import java.util.*

class ShowTaskActivity : AppCompatActivity(), OnMapReadyCallback {

    private val component by lazy { AddTaskComponent.create()}

    private val showTaskViewModel by viewModels<ShowTaskViewModel>{ component.viewModelFactory()}

   private lateinit var observerName: Observer<String?>
   private lateinit var observerDescription: Observer<String?>
   private  lateinit var observerDate: Observer<GregorianCalendar?>
   private lateinit var observerTimeFrom: Observer<String?>
   private lateinit var observerTimeTo: Observer<String?>
   private lateinit var observerImportance: Observer<String?>
   private lateinit var observerAddress: Observer<String?>
   private lateinit var observerContact: Observer<String?>
   private lateinit var observerStatus: Observer<Boolean?>
    private var isCorrectAddress = false

    private var  gmap : GoogleMap? = null

    companion object{
        private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_task)

        initObserve()
        val intent = intent
        if (intent != null) { showTaskViewModel.init(intent) }

        var mapViewBundle : Bundle? = null
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)


       setListeners()
    }

    private fun setListeners() {
        showMapLabel.setOnClickListener {
            showMap()
        }
    }

    private fun showMap() {
        if(mapView.visibility == View.GONE){
            onMapReady(gmap)
            if (isCorrectAddress) {
                mapView.visibility = View.VISIBLE
                showMapLabel.text = getString(R.string.hide_map)
            } else {
                Toast.makeText(this, "Address is incorrect!", Toast.LENGTH_LONG).show()
            }

        } else {
            mapView.visibility = View.GONE
            showMapLabel.text = getString(R.string.showMap)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle : Bundle? = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle =  Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

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
        val  strAddress = showTaskViewModel.taskUI.address.value

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
        val coder = Geocoder(this, loc)
        var lat: Double
        var lon: Double

        coder?.apply {
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


    private fun initObserve() {
        observerName = Observer{
            it?.let{
                nameTextViewShowActivity.text = it
            }
        }
        observerDescription = Observer{
            it?.let{
                descriptionTextViewShowActivity.text = it
            }
        }

        observerDate = Observer{
            it?.let{
                dateTextViewShowActivity.text = "${it.get(GregorianCalendar.DAY_OF_MONTH)}-" +
                        "${it.get(GregorianCalendar.MONTH)}-" +
                        "${it.get(GregorianCalendar.YEAR)}"
            }
        }

        observerTimeFrom = Observer{
            it?.let{
                timeTextViewShowActivity.text = it +" - "+showTaskViewModel.taskUI.timeTo
            }
        }

        observerTimeTo = Observer{
            it?.let{
                timeTextViewShowActivity.text = showTaskViewModel.taskUI.timeFrom.value +" - " + it
            }
        }

        observerImportance = Observer{
            it?.let{
                importanceTextViewShowActivity.text = it
            }
        }

        observerAddress = Observer{
            it?.let{
                addressTextViewShowActivity.text = showTaskViewModel.taskUI.address.value
            }
        }

        observerContact = Observer{
            it?.let{
                    snapContactTextViewShowActivity.text = getString(R.string.tied_contact) + it
            }
        }

        observerStatus = Observer{
            it?.let{
                if(it){
                    statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                    statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                }
                else{
                    statusTrueTextViewShowActivity.setBackgroundColor(getColor(R.color.divider))
                    statusFalseTextViewShowActivity.setBackgroundColor(getColor(R.color.accent))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        showTaskViewModel.taskUI.name.observe(this, observerName)
        showTaskViewModel.taskUI.description.observe(this, observerDescription)
        showTaskViewModel.taskUI.date.observe(this, observerDate)
        showTaskViewModel.taskUI.timeFrom.observe(this, observerTimeFrom)
        showTaskViewModel.taskUI.timeTo.observe(this, observerTimeTo)
        showTaskViewModel.taskUI.importance.observe(this, observerImportance)
        showTaskViewModel.taskUI.address.observe(this, observerAddress)
        showTaskViewModel.taskUI.contact.observe(this, observerContact)
        showTaskViewModel.taskUI.status.observe(this, observerStatus)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        showTaskViewModel.taskUI.name.removeObserver(observerName)
        showTaskViewModel.taskUI.description.removeObserver(observerDescription)
        showTaskViewModel.taskUI.date.removeObserver(observerDate)
        showTaskViewModel.taskUI.timeFrom.removeObserver(observerTimeFrom)
        showTaskViewModel.taskUI.timeTo.removeObserver(observerTimeTo)
        showTaskViewModel.taskUI.importance.removeObserver(observerImportance)
        showTaskViewModel.taskUI.address.removeObserver(observerAddress)
        showTaskViewModel.taskUI.contact.removeObserver(observerContact)
        showTaskViewModel.taskUI.status.removeObserver(observerStatus)
    }
}
