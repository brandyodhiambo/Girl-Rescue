package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.database.AddGuardian
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentAddContactBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.hbb20.CountryCodePicker




class AddContact : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao

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
                CoroutineScope(Dispatchers.Main).launch {
                    val guardians = AddGuardian(
                        0,
                        binding.editTextTextGuardianName.editText?.text.toString(),
                        binding.editTextPhoneGuardian.editText?.text.toString(),
                    )
                    guardianDao.insertGuardian(guardians)
                    findNavController().navigate(R.id.action_addContactFragment_to_contacts)
                }
            }
        }

        return view
    }




}