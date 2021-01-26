package com.ivanasharov.smartplanner.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Network
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.ivanasharov.smartplanner.R
import com.ivanasharov.smartplanner.databinding.WeatherFragmentBinding
import com.ivanasharov.smartplanner.presentation.view.dialogs.PermissionDialogFragment
import com.ivanasharov.smartplanner.presentation.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 125
    }

    private val mWeatherViewModel: WeatherViewModel by viewModels()

    private lateinit var mBinding: WeatherFragmentBinding
    private lateinit var mLocationManager: LocationManager
    private val hasPermission: Boolean = false
    private var hasGPS = false
    private var hasNetwork = false
    private var mLocationGPS: Location? = null
    private var mLocationNetwork: Location? = null
  //  private  lateinit var mLocationGPS: Location
   // private  lateinit var mLocationNetwork: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false)
        mBinding.viewModel = mWeatherViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
        initWeather()
        val toolbar : Toolbar = activity?.findViewById<Toolbar>(R.id.toolbar_actionbar) as Toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        return mBinding.root
    }

    private fun initWeather() {
        if (hasPermissionLocation()) {
           // loadWeather()
            mWeatherViewModel.getData(true)
        } else {
            requestPermissionWithRationale()
          //  mWeatherViewModel.getData(hasPermission)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.updateWeatherMenu) {
            initWeather()
        }
        return super.onOptionsItemSelected(item)
    }
/*    @SuppressLint("MissingPermission")
    private fun loadWeather() {
        Log.d("WE", "ok")
        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasNetwork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        hasGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(hasGPS || hasNetwork){
            if (hasGPS){
                Log.d("WE", "hasGPS")
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object: LocationListener{
                    override fun onLocationChanged(location: Location?) {
                        mLocationGPS = location
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                })
                val localGPSLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGPSLocation != null)
                    mLocationGPS = localGPSLocation
            }

            if (hasNetwork){
                Log.d("WE", "hasNetwork")
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object: LocationListener{
                    override fun onLocationChanged(location: Location?) {
                        mLocationNetwork = location
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                })
                val localNetworkLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(localNetworkLocation != null)
                    mLocationNetwork = localNetworkLocation
            }

            if (mLocationGPS != null && mLocationNetwork != null){
                val radiusGPS = mLocationGPS?.accuracy
                val radiusNetwork = mLocationGPS?.accuracy
                if(radiusGPS != null && radiusNetwork != null && radiusGPS > radiusNetwork){
                    Log.d("WE", "network lat: ${mLocationNetwork?.latitude} and lon: ${mLocationNetwork?.longitude}")
                } else
                    Log.d("WE", "gps lat: ${mLocationGPS?.latitude} and lon: ${mLocationGPS?.longitude}")
            } else if (mLocationGPS != null){
                    Log.d("WE", "network lat: ${mLocationGPS?.latitude} and lon: ${mLocationGPS?.longitude}")
            } else if (mLocationNetwork != null){
                    Log.d("WE", "network lat: ${mLocationNetwork?.latitude} and lon: ${mLocationNetwork?.longitude}")
            } else {
                Log.d("WE", "NO")
            }

        } else {
            Log.d("WE", "NO")
        }
    }*/

    private fun hasPermissionLocation(): Boolean {
        var result = 0
        val permissions =
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        for (permission in permissions) {
            result = PermissionChecker.checkCallingOrSelfPermission(requireContext(), permission)
            if (result != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

/*    private fun requestPermissionWithRationale() {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    val message =
                        "Location permissions are needed to get the weather forecast for your location"
                    Snackbar.make(
                        activity?.findViewById(android.R.id.content) as View,
                        message,
                        Snackbar.LENGTH_INDEFINITE
                    )
                       .setAction("GRANT") {
                            val qq = it.isClickable
                            requestPermsForLocation()
                        }
                }  else {
                    requestPermsForLocation()
                }
    }*/

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
           val dialog = PermissionDialogFragment(object: PermissionDialogFragment.PermissionDialogListener{
               override fun onDialogPositiveClick() {
                   Log.d("WE", "OK")
                   mWeatherViewModel.getData(true)
               }

               override fun onDialogNegativeClick() {
                   Log.d("WE", "NO")
                   mWeatherViewModel.getData(false)
               }
           })
            dialog.show(childFragmentManager, "PermissionDialogFragment")
        }  else {
            requestPermsForLocation()
        }
    }

    private fun requestPermsForLocation() {
        val permissions =
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var allowedLocation = false

        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                allowedLocation = true
                for (res in grantResults) {
                    // if user granted all permissions.
                    allowedLocation =
                        allowedLocation && (res == PackageManager.PERMISSION_GRANTED)
                }
            }
            else -> {
                allowedLocation = false
            }
        }


                if (allowedLocation) {
                   // loadWeather()
                    mWeatherViewModel.getData(true)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(
                                requireContext(),
                                "Location permission denied.",
                                Toast.LENGTH_LONG
                            ).show();
                            mWeatherViewModel.getData(false)
                        } else {
                            showNoPermissionSnackbar()
                        }
                    }
                }
    }

    private fun showNoPermissionSnackbar() {
                Snackbar.make(
                    activity?.findViewById(android.R.id.content) as View,
                    "Location permissions are not granted",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Settings", View.OnClickListener {
                        openApplicationSettings()
                        Toast.makeText(
                            requireContext(),
                            "Open Permissions and grant the location permissions",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    })
                    .show()
            }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + activity?.packageName)
        )
        startActivityForResult(appSettingsIntent, PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
           // loadWeather()
            mWeatherViewModel.getData(true)
        } else{
            mWeatherViewModel.getData(false)
        }
        return
        //super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWeatherViewModel.isDefaultCity.observe(viewLifecycleOwner, Observer {
            if(it)
                Toast.makeText(requireContext(), getString(R.string.result_default_city), Toast.LENGTH_LONG).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("run", "InformationFragment")
    }

}
