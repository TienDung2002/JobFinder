package com.example.jobfinder.Datas.Model

import android.os.Parcel
import android.os.Parcelable

data class SupportUser(
    var supportId: String? = null,
    var supportName: String? = null,
    var status: String? = null,
    var description: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(supportId)
        parcel.writeString(supportName)
        parcel.writeString(status)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SupportUser> {
        override fun createFromParcel(parcel: Parcel): SupportUser {
            return SupportUser(parcel)
        }

        override fun newArray(size: Int): Array<SupportUser?> {
            return arrayOfNulls(size)
        }
    }
}
