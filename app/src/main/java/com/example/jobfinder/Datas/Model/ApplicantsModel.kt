package com.example.jobfinder.Datas.Model

import android.os.Parcel
import android.os.Parcelable

class ApplicantsModel(
    var userId: String?,
    var applicantDes: String?,
    var appliedDate: String?,
    var userName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(applicantDes)
        parcel.writeString(appliedDate)
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApplicantsModel> {
        override fun createFromParcel(parcel: Parcel): ApplicantsModel {
            return ApplicantsModel(parcel)
        }

        override fun newArray(size: Int): Array<ApplicantsModel?> {
            return arrayOfNulls(size)
        }
    }
}
