package com.adhanjadevelopers.girl_rescue.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }
}