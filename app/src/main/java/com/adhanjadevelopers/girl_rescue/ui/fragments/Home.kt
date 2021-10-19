package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var guardianDao :GuardianDao
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var sentPendingIntent: PendingIntent
    private lateinit var deliveredPendingIntent: PendingIntent

    val sms: SmsManager? = SmsManager.getDefault()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sentPendingIntent = PendingIntent.getBroadcast(requireContext(),0,Intent("SMS_SENT_ACTION"),0)
        deliveredPendingIntent = PendingIntent.getBroadcast(requireContext(),0,Intent("SMS_DELIVERED_ACTION"),0)

        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        binding.helpButton.setOnClickListener {
            Toast.makeText(requireContext(), "Helped Clicked", Toast.LENGTH_SHORT).show()
            displayDialog()
        }
        return binding.root
    }

    private fun displayDialog(){
        AlertDialog.Builder(requireContext())
                .setTitle("Emergency")
            .setMessage("Are you sure you want to send this emergency message to guardians?")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                guardianDao.getAllGuardian().forEach { details ->
                    sms?.sendTextMessage(details.phoneNumber, null, "Hey I am in danger", sentPendingIntent, deliveredPendingIntent)
                }
                }
            }
            .setNegativeButton("No",null)
            .show()
    }

}