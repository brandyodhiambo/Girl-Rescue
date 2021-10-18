package com.adhanjadevelopers.girl_rescue.ui.activities

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
import android.widget.ImageView
import android.widget.TextView
import com.adhanjadevelopers.girl_rescue.R
import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

        fetchUserProfile ()

        val headerView = binding.navigationView.getHeaderView(0)
        userName = headerView.findViewById(R.id.headerName)
        userEmail = headerView.findViewById(R.id.headerEmail)
        userPhone = headerView.findViewById(R.id.headerPhone)
        image = headerView.findViewById(R.id.user_profile_image)

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
                userName.text = snapshot.child("name").value.toString()
                userPhone.text = snapshot.child("phoneNumber").value.toString()
                userEmail.text = snapshot.child("email").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }
}