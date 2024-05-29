package com.example.jobfinder.Datas.Model

import android.os.Parcel
import android.os.Parcelable

class JobModel(
    var jobId: String? = null,
    var jobTitle: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var empAmount: String? = null,
    var salaryPerEmp: String? = null,
    var address: String? = null,
    var jobDes: String? = null,
    var totalSalary: String? = null,
    var postDate: String? = null,
    var numOfRecruited: String? = null,
    var BUserName: String? = null,
    var jobType: String? = null,
    var BUserId: String? = null,
    var status: String? = null,
    var startHr: String? = null,
    var endHr: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        jobId = parcel.readString(),
        jobTitle = parcel.readString(),
        startTime = parcel.readString(),
        endTime = parcel.readString(),
        empAmount = parcel.readString(),
        salaryPerEmp = parcel.readString(),
        address = parcel.readString(),
        jobDes = parcel.readString(),
        totalSalary = parcel.readString(),
        postDate = parcel.readString(),
        numOfRecruited = parcel.readString(),
        BUserName = parcel.readString(),
        jobType = parcel.readString(),
        BUserId = parcel.readString(),
        status = parcel.readString(),
        startHr = parcel.readString(),
        endHr = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jobId)
        parcel.writeString(jobTitle)
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
        parcel.writeString(jobType)
        parcel.writeString(BUserId)
        parcel.writeString(status)
        parcel.writeString(startHr)
        parcel.writeString(endHr)
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
