package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.FragmentContactsBinding


class Contacts : Fragment() {

    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container,false)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_contacts_to_addContact);
        }


        return binding.root
    }
}