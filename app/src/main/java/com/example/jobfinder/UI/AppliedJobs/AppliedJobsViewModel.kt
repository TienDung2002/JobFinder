package com.example.jobfinder.UI.AppliedJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.JobModel

class AppliedJobsViewModel: ViewModel()  {
    private val appliedList: MutableList<AppliedJobModel> = mutableListOf()
    private val _appliedListLiveData = MutableLiveData<List<AppliedJobModel>>()


    val appliedListLiveData: LiveData<List<AppliedJobModel>> get() = _appliedListLiveData


    fun getAppliedList(): List<AppliedJobModel>{
        return appliedList
    }

    fun clearAppliedList(){
        appliedList.clear()
        _appliedListLiveData.value = appliedList
    }

    fun addAppliedToAppliedList(AppliedJobsData: AppliedJobModel) {
        appliedList.add(AppliedJobsData)
        _appliedListLiveData.value = appliedList
    }
}