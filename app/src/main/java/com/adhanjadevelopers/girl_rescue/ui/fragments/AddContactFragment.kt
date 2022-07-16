package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.database.AddGuardian
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentAddContactBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.hbb20.CountryCodePicker
import java.util.*


class AddContact : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao
    private lateinit var locationManager: LocationManager
    private lateinit var map:GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1

    var latitude: String? = null
    var longitude: String? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap



        val latitude = 0.570044
        val longitude = 34.559244
        val zoomLevel = 18f
        val homeLatLng = LatLng(latitude, longitude)

        googleMap.addMarker(
            MarkerOptions()
            .position(homeLatLng)
            .title("Am Here")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
       enableMyLocation()
       // setMapLongClick(map)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        guardianDatabase = GuardianDatabase.getInstance(application)
        guardianDao = GuardianDatabase.getInstance(application).guardianDao


        binding.addContact.setOnClickListener {
            if (binding.editTextTextGuardianName.editText?.text.toString().isBlank()) {
                binding.editTextTextGuardianName.error = "Required"
                return@setOnClickListener
            } else if (binding.editTextPhoneGuardian.editText?.text.toString().isBlank()) {
                binding.editTextPhoneGuardian.error = "Required"
                return@setOnClickListener
            } else if (binding.editTextPhoneGuardian.editText?.text!!.length < 10){
                binding.editTextPhoneGuardian.error = "Phone Number is too short"
            }
            else if (binding.editTextPhoneGuardian.editText?.text!!.length > 10){
                binding.editTextPhoneGuardian.error = "Phone Number is too long"
            }
            else {
                locationManager =
                    requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS()
                } else {
                    getLocationn()
                    if(latitude==null || longitude== null){
                        return@setOnClickListener
                    }else {
                        CoroutineScope(Dispatchers.Main).launch {
                            val guardians = AddGuardian(
                                id=0,
                                name = binding.editTextTextGuardianName.editText?.text.toString(),
                                phoneNumber = binding.editTextPhoneGuardian.editText?.text.toString(),
                                longitude = longitude,
                                latitide = latitude

                            )
                            guardianDao.insertGuardian(guardians)
                            findNavController().navigate(R.id.action_addContactFragment_to_contacts)
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapsFragment = childFragmentManager.findFragmentById(R.id.addMap) as SupportMapFragment?
        mapsFragment?.getMapAsync(callback)
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            map.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                requireContext() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun getLocationn(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }else{
            val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                val lat = locationGPS.latitude
                val longi = locationGPS.longitude

                latitude = lat.toString()
                longitude = longi.toString()
                Toast.makeText(requireContext(), "found location ${latitude} ${longitude}", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Unable to find location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun OnGPS() {
        AlertDialog.Builder(requireContext())
            .setMessage("Enable GPS ")
            .setCancelable(false)
            .setPositiveButton(
                "yes"
            ) { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, which -> dialog?.cancel() }
            .show()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                enableMyLocation()
        }
    }



}

