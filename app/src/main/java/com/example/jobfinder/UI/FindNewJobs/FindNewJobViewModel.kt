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
    private var _filteredJobList = MutableLiveData<List<JobModel>>()
    private var _postedJobList = MutableLiveData<List<JobModel>>()
    var _isLoading = MutableLiveData<Boolean>()


    val postedJobList: LiveData<List<JobModel>> get() = _postedJobList
    val filteredJobList: LiveData<List<JobModel>> get() = _filteredJobList



    fun addJobsData(jobsData: List<JobModel>) {
        JobsList.clear()
        JobsList.addAll(jobsData)
        _postedJobList.value = JobsList
    }

    fun updateFilteredJobList(filteredList: List<JobModel>) {
        _filteredJobList.value = filteredList
    }



}
