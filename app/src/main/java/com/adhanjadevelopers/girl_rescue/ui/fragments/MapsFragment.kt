package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
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

import android.content.Context
import android.location.LocationManager
import com.adhanjadevelopers.girl_rescue.R
import androidx.constraintlayout.motion.widget.Debug.getLocation

import androidx.core.content.ContextCompat.getSystemService
import android.content.DialogInterface

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.media.audiofx.EnvironmentalReverb
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import java.util.*

private const val TAG = "MapsFragment"

class MapsFragment : Fragment() {
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    //private lateinit var mLocation:Location
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

/*
    *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
*/

        val latitude = 0.570044
        val longitude = 34.559244
        val zoomLevel = 18f
        val homeLatLng = LatLng(latitude, longitude)

        googleMap.addMarker(MarkerOptions()
            .position(homeLatLng)
            .title("Am Here"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        //setMapLongClick(map)
        enableMyLocation()
        setPoiClick(map)
       // addguardian(mLocation,map)


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.fragment_maps,
            container,
            false
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
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

    /*private fun setMapLongClick(map: GoogleMap){
        map.setOnMapLongClickListener { latlng->
            val circle = CircleOptions()
                .center(latlng)
                .fillColor(Color.BLUE)
                .strokeColor(Color.BLACK)
                .strokeWidth(2f)
                .radius(50.0)
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
    }*/
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

   /* private fun addguardian(location: Location,map: GoogleMap){
        mLocation = location
        val latLng = LatLng(location.latitude,location.longitude)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Am Here")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
           // .snippet(snippet)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        map.addMarker(markerOptions).showInfoWindow()
    }*/

   /* private fun getLocationn(){
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

                Toast.makeText(applicationContext, "found location ${latitude} ${longitude}", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Unable to find location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

}