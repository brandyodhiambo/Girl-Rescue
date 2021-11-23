package com.adhanjadevelopers.girl_rescue.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.ActivityDashboardBinding
import com.adhanjadevelopers.girl_rescue.databinding.ActivitySignUpBinding
import com.adhanjadevelopers.girl_rescue.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")

        binding.buttonSignUp.setOnClickListener {

            binding.progressBarSignUp.isVisible = true

            if (binding.editTextFullName.text.toString().isNullOrEmpty()) {
                binding.editTextFullName.error = "Required Full Names"
                return@setOnClickListener
            } else if (binding.editTextEmail.text.toString().isNullOrEmpty()) {
                binding.editTextEmail.error = "Required Email"
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.text.toString()).matches()) {
                binding.editTextEmail.error = "Email Not Valid"
                return@setOnClickListener
            }else if (binding.editTextPhoneNumber.text.toString().isNullOrEmpty()) {
                binding.editTextPhoneNumber.error = "Required Phone Number"
                return@setOnClickListener
            } else if(binding.editTextPhoneNumber.text.length!=10){
                binding.editTextPhoneNumber.error = "Short Phone Number"
            }else if (binding.editTextPassword.text.toString().isNullOrEmpty()) {
                binding.editTextPassword.error = "Required Password"
                return@setOnClickListener
            } else if (binding.editTextPassword.text.length<8) {
                binding.editTextPassword.error = "Password Strength Weak"
                return@setOnClickListener
            }else if (binding.editTextConfirmPassword.text.toString().isNullOrEmpty()) {
                binding.editTextConfirmPassword.error = "Required Password"
                return@setOnClickListener
            }else if (binding.editTextConfirmPassword.text.length<8) {
                binding.editTextConfirmPassword.error = "Password Strength Weak"
                return@setOnClickListener
            } else {

                val name = binding.editTextFullName.text.toString()
                val email = binding.editTextEmail.text.toString()
                val phoneNumber = binding.editTextPhoneNumber.text.toString()
                val password = binding.editTextPassword.text.toString()
                val confirmPassword = binding.editTextConfirmPassword.text.toString()


                if (password == confirmPassword) {

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val firebaseUser = firebaseAuth.currentUser
                                val user = User(name, email, phoneNumber)

                                //databaseReference.child(firebaseUser!!.uid).setValue(user)
                                databaseReference.child("users").child(firebaseUser!!.uid)
                                    .setValue(user)
                                val profileChangeRequest = UserProfileChangeRequest.Builder()
                                    .setDisplayName(binding.editTextFullName.getText().toString())
                                    .build()

                                firebaseUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            firebaseUser.sendEmailVerification()
                                                .addOnSuccessListener {
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(
                                                            this,
                                                            "Verification Email Sent To Your Email.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            task.exception!!.message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    }
                                val intent =
                                    Intent(this@SignUpActivity, SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(this@SignUpActivity, "Welcome", Toast.LENGTH_SHORT)
                                    .show()
                                binding.progressBarSignUp.setVisibility(View.INVISIBLE)
/*
                                firebaseUser.sendEmailVerification().addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Verification sent check your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBarSignUp.isVisible =false
                                    startActivity(Intent(this,SignInActivity::class.java))
                                    finish()

                                }.addOnFailureListener {
                                    Log.d(
                                        TAG,
                                        "onCreate: verification not sent ${it.localizedMessage}"
                                    )
                                    binding.progressBarSignUp.isVisible =false
                                }*/

                            } else {
                                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                                binding.progressBarSignUp.isVisible =false
                            }
                        }.addOnFailureListener {
                        Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                            binding.progressBarSignUp.isVisible =false
                    }


                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    binding.progressBarSignUp.isVisible =false
                }

            }
        }

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }


    }
}