package com.adhanjadevelopers.girl_rescue.ui.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.adhanjadevelopers.girl_rescue.databinding.ActivityMainBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userPhone : TextView
    private lateinit var image : ImageView
    private lateinit var databaseReference: DatabaseReference
   // private lateinit var imageUrl : String

    //sensorManager and accelerations
    private var sensorManager: SensorManager? = null
    private var accelaration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private lateinit var guardianDao: GuardianDao
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var sentPendingIntent: PendingIntent
    private lateinit var deliveredPendingIntent: PendingIntent
    private lateinit var locationManager: LocationManager
    private var MY_PERMISSIONS_REQUEST_SEND_SMS = 1

    var latitude: String? = null
    var longitude: String? = null

    val sms: SmsManager? = SmsManager.getDefault()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")




        navController = Navigation.findNavController(this, R.id.fragment)

        // setup bottom navigation
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        //setup top back button
        appBarConfiguration =
            AppBarConfiguration.Builder(navController.graph).setOpenableLayout(binding.drawer)
                .build()
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawer)

        //setup Navigation Drawer
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        val headerView = binding.navigationView.getHeaderView(0)
        userName = headerView.findViewById(R.id.headerName)
        userEmail = headerView.findViewById(R.id.headerEmail)
        userPhone = headerView.findViewById(R.id.headerPhone)
        image = headerView.findViewById(R.id.user_profile_image)

        val firebaseUser = firebaseAuth.currentUser
        val userid =firebaseUser!!.uid
        databaseReference.child(userid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName.text= snapshot.child("name").value.toString()
                userPhone.text = snapshot.child("phoneNumber").value.toString()
                userEmail.text = snapshot.child("email").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        //fetchUserProfile ()

        //logout
        val logout = binding.navigationView.menu.findItem(R.id.logout)
        logout.setOnMenuItemClickListener {
            Toast.makeText(this@MainActivity, "Sign out", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        //share
        val share = binding.navigationView.menu.findItem(R.id.share)
        share.setOnMenuItemClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Girl Rescue")
            startActivity(Intent.createChooser(intent, "Choose One!"))
            true
        }

        sentPendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, Intent("SMS_SENT_ACTION"), 0)
        deliveredPendingIntent =
            PendingIntent.getBroadcast(applicationContext, 0, Intent("SMS_DELIVERED_ACTION"), 0)

        guardianDatabase = GuardianDatabase.getInstance(applicationContext)
        guardianDao = guardianDatabase.guardianDao

        locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS()
        } else {
            getLocationn()
            if(latitude==null || longitude== null){
                return
            }else {
               // displayDialog(latitude!!, longitude!!)
                //Shaking Event
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

                Objects.requireNonNull(sensorManager)!!
                    .registerListener(sensorListener,sensorManager!!
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

                accelaration = 10f
                currentAcceleration = SensorManager.GRAVITY_EARTH
                lastAcceleration  = SensorManager.GRAVITY_EARTH
            }
        }




    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            accelaration = accelaration * 0.9f + delta

            if (accelaration>12){
                Toast.makeText(applicationContext, "Sake efent detected", Toast.LENGTH_SHORT).show()
                if ((ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_SMS) +
                            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS))
                    != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            Manifest.permission.SEND_SMS)){
                        return
                    }
                    val sendMessage:Boolean = true
                    if (sendMessage == true){
                        CoroutineScope(Dispatchers.IO).launch {
                            guardianDao.getAllGuardian().forEach { details ->
                                sms?.sendTextMessage(
                                    details.phoneNumber,
                                    null,
                                    "Hey I am in danger!!!"+" My location is "+"https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}" ,
                                    sentPendingIntent,
                                    deliveredPendingIntent
                                )
                            }
                        }
                        Toast.makeText(applicationContext, "Messages sent", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Add Guardian ", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    ActivityCompat.requestPermissions(
                        applicationContext as Activity,
                        arrayOf(Manifest.permission.SEND_SMS),
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuraacy: Int) {
            TODO("Not yet implemented")
        }
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    //update header details
   private fun fetchUserProfile (){
        val firebaseUser = firebaseAuth.currentUser
        val userid =firebaseUser!!.uid
        databaseReference.child(userid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName.text= snapshot.child("name").value.toString()
                userPhone.text = snapshot.child("phoneNumber").value.toString()
                userEmail.text = snapshot.child("email").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

    private fun getLocationn(){
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
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
    }

    private fun OnGPS() {
        AlertDialog.Builder(applicationContext)
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
}