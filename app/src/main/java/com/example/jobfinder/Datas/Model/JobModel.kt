package com.example.jobfinder.Datas.Model

import android.os.Parcel
import android.os.Parcelable

class JobModel(
    var jobId: String?,
    var jobTitle: String?,
    var shift: String?,
    var startTime: String?,
    var endTime: String?,
    var empAmount: String?,
    var salaryPerEmp: String?,
    var address: String?,
    var jobDes: String?,
    var totalSalary: String?,
    var postDate: String?,
    var numOfRecruited: String?,
    var BUserName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jobId)
        parcel.writeString(jobTitle)
        parcel.writeString(shift)
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeString(empAmount)
        parcel.writeString(salaryPerEmp)
        parcel.writeString(address)
        parcel.writeString(jobDes)
        parcel.writeString(totalSalary)
        parcel.writeString(postDate)
        parcel.writeString(numOfRecruited)
        parcel.writeString(BUserName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JobModel> {
        override fun createFromParcel(parcel: Parcel): JobModel {
            return JobModel(parcel)
        }

        override fun newArray(size: Int): Array<JobModel?> {
            return arrayOfNulls(size)
        }
    }
}
