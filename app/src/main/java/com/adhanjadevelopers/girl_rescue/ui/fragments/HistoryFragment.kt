package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.adapters.GuardianAdapter
import com.adhanjadevelopers.girl_rescue.adapters.HistoryAdapter
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentHistoryBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {
   private lateinit var binding: FragmentHistoryBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao
    private val adapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        GlobalScope.launch {
            val myList = guardianDao.getAllHistory()
            adapter.submitList(myList)
            binding.historyRecycler.adapter = adapter

        }

        return binding.root
    }


}