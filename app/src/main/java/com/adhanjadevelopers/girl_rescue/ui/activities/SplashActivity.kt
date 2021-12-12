package com.adhanjadevelopers.girl_rescue.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.adhanjadevelopers.girl_rescue.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val user = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed(Runnable {
            if (user != null && user.isEmailVerified){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            else{
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }

        },3000)
    }
}