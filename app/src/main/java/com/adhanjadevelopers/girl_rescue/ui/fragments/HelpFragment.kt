package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root

        val spinner = binding.spComment
        val arrayAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item
        )
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    "You selected ${adapterView?.getItemAtPosition(position).toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.btFeedback.setOnClickListener {
            Toast.makeText(requireContext(), "Feedback has been sent", Toast.LENGTH_SHORT).show()
        }
    }


}