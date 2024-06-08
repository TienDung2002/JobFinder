package com.example.jobfinder.Datas.Model

class CheckInFromBUserModel (
    var NUserId :String?,
    var date:String?,
    var checkInTime: String?,
    var checkOutTime:String?,
    var status: String?
){
    constructor() : this( null, null,null, null, null)
}