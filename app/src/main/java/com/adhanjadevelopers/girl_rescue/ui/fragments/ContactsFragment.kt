package com.adhanjadevelopers.girl_rescue.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.adapters.GuardianAdapter
import com.adhanjadevelopers.girl_rescue.database.AddGuardian
import com.adhanjadevelopers.girl_rescue.database.GuardianDao
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import com.adhanjadevelopers.girl_rescue.databinding.FragmentContactsBinding
import com.adhanjadevelopers.girl_rescue.utils.ItemClickListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Contacts : Fragment(),ItemClickListener {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao
    private lateinit var adapter: GuardianAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container,false)

        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        adapter = GuardianAdapter(this)

        GlobalScope.launch {
            val myList = guardianDao.getAllGuardian()
            adapter.submitList(myList)
            binding.guardianRecycler.adapter = adapter

        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_contacts_to_addContactFragment)
        }
        return binding.root
    }

    override fun deleteGuardian(guardian: AddGuardian, position: Int) {
        GlobalScope.launch {
            guardianDao.deleteGuardian(guardian)
        }
    }

    override fun editGuardian(guardian: AddGuardian, position: Int) {
        findNavController().navigate(R.id.action_contacts_to_addContactFragment)
    }

    /*private fun recyclerDelete(position:Int) {
        val itemTouchHelper:ItemTouchHelper.SimpleCallback = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                    return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

        }

    }*/
}


