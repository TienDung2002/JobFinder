package com.example.jobfinder.Datas.Model

class AppliedJobModel (
    var  buserId :String?,
    var jobId :String?,
    var appliedDate: String?,
    var jobTitle: String?,
    var startHr :String?,
    var endHr: String?,
    var salary:String?,
    var postedDate: String?
    ){
    constructor() : this(null, null, null, null, null, null, null, null)
}