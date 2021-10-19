package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.adapters.GuardianAdapter
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentContactsBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Contacts : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao
    private val adapter by lazy { GuardianAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container,false)

        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        GlobalScope.launch {
            val myList = guardianDao.getAllGuardian()
            adapter.submitList(myList)
            binding.guardianRecycler.adapter = adapter

        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }


        return binding.root
    }
}