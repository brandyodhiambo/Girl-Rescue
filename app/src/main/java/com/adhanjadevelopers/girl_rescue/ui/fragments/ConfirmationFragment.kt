package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.FragmentConfirmationBinding

class ConfirmationFragment : DialogFragment() {
   private lateinit var binding: FragmentConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmationBinding.inflate(inflater,container,false)

        binding.cancel.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.agree.setOnClickListener {
            Toast.makeText(requireActivity(), "yes", Toast.LENGTH_SHORT).show()
        }

        return binding.root

    }


}