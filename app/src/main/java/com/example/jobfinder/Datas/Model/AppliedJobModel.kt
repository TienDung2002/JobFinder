package com.example.jobfinder.Datas.Model

import android.os.Parcel
import android.os.Parcelable

class AppliedJobModel: Parcelable {
    var buserId :String?= null
    var jobId :String?= null
    var appliedDate: String?= null
    var jobTitle: String?= null
    var startHr :String?= null
    var endHr: String?= null
    var salary:String?= null
    var postedDate: String? = null
    constructor() {
        // Constructor mặc định không làm gì cả
    }

    constructor(
        buserId :String?,
        jobId :String?,
        appliedDate: String?,
        jobTitle: String?,
        startHr :String?,
        endHr: String?,
        salary:String?,
        postedDate: String?
    ) {
        this.buserId = buserId
        this.jobId = jobId
        this.appliedDate = appliedDate
        this.jobTitle = jobTitle
        this.startHr = startHr
        this.endHr = endHr
        this.salary = salary
        this.postedDate = postedDate
    }
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(buserId)
        parcel.writeString(jobId)
        parcel.writeString(appliedDate)
        parcel.writeString(jobTitle)
        parcel.writeString(startHr)
        parcel.writeString(endHr)
        parcel.writeString(salary)
        parcel.writeString(postedDate)
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