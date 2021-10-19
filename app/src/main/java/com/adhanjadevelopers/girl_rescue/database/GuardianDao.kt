package com.adhanjadevelopers.girl_rescue.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GuardianDao {
    @Insert
    suspend fun insertGuardian(guardian: AddGuardian)

    @Delete
    fun deleteGuardian(guardian: AddGuardian)

    @Query("SELECT * FROM `guardian_table` ORDER BY id ASC")
    fun getAllGuardian(): List<AddGuardian>

    @Update
    fun updateGuardian(guardian: AddGuardian)
}