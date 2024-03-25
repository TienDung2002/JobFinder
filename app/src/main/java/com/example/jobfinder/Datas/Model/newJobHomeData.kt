package com.example.jobfinder.Datas.Model

data class newJobHomeData(
    var jobId :String?,
    var BUserName: String?,
    var jobTitle : String?,
    var shift : String?,
    var startTime: String?,
    var endTime: String?,
    var empAmount: String?, // số nhân viên yêu cầu
    var numOfRecruited: String?,  // số nhân viên đã tuyển
    var salaryPerEmp: String?,  // lương theo ngày
    var address: String?,       // địa chỉ
    var jobDes: String?,        // mô tả công việc
    var totalSalary: String?,   // tổng lương
    var postDate: String?,      // ngày đăng
)