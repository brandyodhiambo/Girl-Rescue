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

    //history
    @Insert
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM `history_table` ORDER BY id ASC")
    fun getAllHistory(): List<History>

    @Delete
    fun deleteHistory(history: History)

}