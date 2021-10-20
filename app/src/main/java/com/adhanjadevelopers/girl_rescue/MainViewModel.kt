package com.adhanjadevelopers.girl_rescue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adhanjadevelopers.girl_rescue.database.AddGuardian
import com.adhanjadevelopers.girl_rescue.database.GuardianDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val database: GuardianDatabase): ViewModel() {

    private val guardianDao = database.guardianDao

    private val _allGuardians = MutableLiveData<List<AddGuardian>>()
    val allGuardians: LiveData<List<AddGuardian>>  =_allGuardians


 /*   private fun getGuardians(){
        viewModelScope.launch(Dispatchers.IO) {
            _allGuardians.value = guardianDao.getAllGuardian()
        }
    }*/

    fun deleteGuardian(guardian: AddGuardian){
        guardianDao.deleteGuardian(guardian)
    }

}