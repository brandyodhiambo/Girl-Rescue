package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.adhanjadevelopers.girl_rescue.R

import android.graphics.Color
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "MapsFragment"

class MapsFragment : Fragment() {
    private lateinit var map: GoogleMap
    private lateinit var guardianDao: GuardianDao
    private val REQUEST_LOCATION_PERMISSION = 1
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap



        val latitude = 0.570044
        val longitude = 34.559244
        val zoomLevel = 18f
        val homeLatLng = LatLng(latitude, longitude)

        googleMap.addMarker(MarkerOptions()
            .position(homeLatLng)
            .title("Am Here"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        enableMyLocation()
        getGuardianLocation(map)
        setPoiClick(map)
        setMapLongClick(map)


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        guardianDao = GuardianDatabase.getInstance(application).guardianDao
        val view = inflater.inflate(
            R.layout.fragment_maps,
            container,
            false
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.addMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {


                return
            }
            map.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                requireContext() as Activity, arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
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

    private fun setMapLongClick(map: GoogleMap){
        map.setOnMapLongClickListener { latlng->
            val circle = CircleOptions()
                .center(latlng)
                .fillColor(Color.BLUE)
                .strokeColor(Color.BLACK)
                .strokeWidth(2f)
                .radius(20.0)
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latlng.latitude,
                latlng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title("Guardian")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .snippet(snippet)
            )
            map.addCircle(circle)
        }
    }
    private fun setPoiClick(map: GoogleMap){
        map.setOnPoiClickListener { poi->
            val poiMaker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMaker.showInfoWindow()
        }
    }

    private fun getGuardianLocation(map: GoogleMap){
     GlobalScope.launch {
         withContext(Dispatchers.Main){
             val guardianLocation = guardianDao.getAllGuardian()
             guardianLocation.forEach { guardianLocation ->
                 val latitude = guardianLocation.latitide
                 val longitude = guardianLocation.longitude
                 val latlng = latitude?.let { longitude?.let { it1 ->
                     LatLng(it?.toDouble(),
                         it1?.toDouble())
                 } }
                 val snippet = String.format(
                     Locale.getDefault(),
                     "Lat: %1$.5f, Long: %2$.5f",
                     latlng?.latitude,
                     latlng?.longitude
                 )
                 map.addMarker(
                     MarkerOptions()
                         .position(latlng)
                         .title(guardianLocation.name)
                         .draggable(true)
                         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                         .snippet(snippet)
                 )
             }
         }
     }

    }

 }