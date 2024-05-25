package com.example.jobfinder.UI.AppliedJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.JobModel
import com.google.firebase.database.FirebaseDatabase

class AppliedJobsViewModel: ViewModel()  {
    private val appliedList: MutableList<AppliedJobModel> = mutableListOf()
    private val _appliedListLiveData = MutableLiveData<List<AppliedJobModel>>()

    private val database = FirebaseDatabase.getInstance().getReference("AppliedJob")



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

    fun cancelAppliedJob(jobId: String, uid:String) {
        database.child(uid).child(jobId).removeValue()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    fun deleteAppliedJob(jobId:String){
        database.get().addOnSuccessListener {
            for(uid in it.children){
                database.child(uid.key.toString()).child(jobId).removeValue()
            }
        }
    }
}