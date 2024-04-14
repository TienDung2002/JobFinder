package com.example.jobfinder.UI.FindNewJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FindNewJobViewModel : ViewModel() {
    private val JobsList: MutableList<JobModel> = mutableListOf()
    private val _jobsListLiveData = MutableLiveData<List<JobModel>>()
    var _isLoading = MutableLiveData<Boolean>()

    val jobsListLiveData: LiveData<List<JobModel>> get() = _jobsListLiveData


    fun getJobsList(): List<JobModel> {
        return JobsList
    }
    fun addJobsToJobsList(JobsData: JobModel) {
        JobsList.add(JobsData)
        _jobsListLiveData.value = JobsList
    }

}
