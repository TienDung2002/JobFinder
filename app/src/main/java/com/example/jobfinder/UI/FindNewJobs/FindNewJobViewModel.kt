package com.example.jobfinder.UI.FindNewJobs

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.newJobHomeData

class FindNewJobViewModel:ViewModel() {
    private val JobsList: MutableList<newJobHomeData> = mutableListOf()

    // Thêm dữ liệu vào JobsList
    fun addJobsData(JobsData: newJobHomeData) {
        JobsList.add(JobsData)
    }

    // Lấy danh sách dữ liệu cho adapter.
    fun getJobsList(): List<newJobHomeData> {
        return JobsList
    }
}