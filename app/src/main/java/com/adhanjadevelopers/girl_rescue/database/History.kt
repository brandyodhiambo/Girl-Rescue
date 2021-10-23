package com.adhanjadevelopers.girl_rescue.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history_table")
class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val guardianName:String?=null,
    val guardianPhone:String?=null,
    val date:String? =null)
