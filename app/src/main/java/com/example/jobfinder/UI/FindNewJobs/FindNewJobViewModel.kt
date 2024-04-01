package com.example.jobfinder.UI.FindNewJobs

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel

class FindNewJobViewModel:ViewModel() {
    private val JobsList: MutableList<JobModel> = mutableListOf()

    // Thêm dữ liệu vào JobsList
    fun addJobsData(JobsData: JobModel) {
        JobsList.add(JobsData)
    }

    // Lấy danh sách dữ liệu cho adapter.
    fun getJobsList(): List<JobModel> {
        return JobsList
    }
}