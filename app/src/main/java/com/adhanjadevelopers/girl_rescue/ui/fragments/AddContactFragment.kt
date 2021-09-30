package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.content.Intent
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
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        guardianDatabase = GuardianDatabase.getInstance(requireContext())
        guardianDao = guardianDatabase.guardianDao

        binding.addContact.setOnClickListener {
            if (binding.editTextTextGuardianName.text.toString().isBlank()) {
                binding.editTextTextGuardianName.error = "Required"
                return@setOnClickListener
            } else if (binding.editTextPhoneGuardian.text.toString().isBlank()) {
                binding.editTextPhoneGuardian.error = "Required"
                return@setOnClickListener
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val guardians = AddGuardian(
                        0,
                        binding.editTextTextGuardianName.text.toString(),
                        binding.editTextPhoneGuardian.text.toString(),
                        Glide.with(binding.guardianAddedImage).load()

                    )
                    guardianDao.insertGuardian(guardians)
                    findNavController().navigate(R.id.action_addContactFragment_to_contactsFragment)

                }
            }
        }


        return view
    }


}