package com.adhanjadevelopers.girl_rescue.database

import androidx.room.*

@Dao
interface GuardianDao {
    @Insert
    suspend fun insertGuardian(guardian: AddGuardian)

    @Delete
    fun deletGuardian(guardian: AddGuardian)

    @Query("SELECT * FROM `guardian_table` ORDER BY id ASC")
    fun getAllGuardian():List<AddGuardian>

    @Update
    fun updateGuardian(guardian: AddGuardian)
}