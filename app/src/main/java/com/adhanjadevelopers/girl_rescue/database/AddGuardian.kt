package com.adhanjadevelopers.girl_rescue.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guardian_table")

class AddGuardian(
    @PrimaryKey(autoGenerate = true)
    val id : Int =0,
    val name : String? = null,
    val phoneNumber : String? = null,
    val imageUrl : String? = "https://www.w3schools.com/w3images/avatar6.png"



) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddGuardian> {
        override fun createFromParcel(parcel: Parcel): AddGuardian {
            return AddGuardian(parcel)
        }

        override fun newArray(size: Int): Array<AddGuardian?> {
            return arrayOfNulls(size)
        }
    }
}
