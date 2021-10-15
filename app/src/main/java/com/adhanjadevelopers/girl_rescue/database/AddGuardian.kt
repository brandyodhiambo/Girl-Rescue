package com.adhanjadevelopers.girl_rescue.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guardian_table")

class AddGuardian(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0,
    val name : String? = null,
    val phoneNumber : String? = null,
    val imageUrl : String = "https://www.w3schools.com/w3images/avatar6.png"
)
