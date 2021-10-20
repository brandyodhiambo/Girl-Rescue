package com.adhanjadevelopers.girl_rescue.utils

import com.adhanjadevelopers.girl_rescue.database.AddGuardian

interface ItemClickListener {
    fun deleteGuardian(guardian: AddGuardian, position:Int)
    fun editGuardian(guardian: AddGuardian,position: Int)
}