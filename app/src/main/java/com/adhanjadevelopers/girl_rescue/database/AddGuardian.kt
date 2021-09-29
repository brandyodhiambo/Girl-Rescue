package com.adhanjadevelopers.girl_rescue.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Guardian-table")

data class AddGuardian(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0,
    val name : String? = null,
    val phoneNumber : String? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image : ByteArray

)
