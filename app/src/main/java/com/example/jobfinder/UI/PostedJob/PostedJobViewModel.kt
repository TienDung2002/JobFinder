package com.example.jobfinder.UI.PostedJob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PostedJobViewModel : ViewModel() {
    private val _postedJobList = MutableLiveData<List<JobModel>>()
    val postedJobList: LiveData<List<JobModel>> get() = _postedJobList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid
    private val database = FirebaseDatabase.getInstance().getReference("Job").child(uid.toString())

    interface OnJobDeletedListener {
        fun onJobDeleted()
    }

    fun fetchPostedJobs() {
        _isLoading.value = true
        database.get().addOnSuccessListener { dataSnapshot ->
            val postedJobList: MutableList<JobModel> = mutableListOf()
            for (jobSnapshot in dataSnapshot.children) {
                val jobModel = jobSnapshot.getValue(JobModel::class.java)
                jobModel?.let {
                    // Update status based on start time and end time
                    it.status = GetData.getStatus(it.startTime.toString(), it.endTime.toString(), it.empAmount.toString(), it.numOfRecruited.toString())
                    // Add the updated jobModel to the list
                    postedJobList.add(it)
                }
            }
            val sortedPostedJobList = postedJobList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
            _postedJobList.value = sortedPostedJobList
            // Update status to Firebase
            updateStatusToFirebase(sortedPostedJobList)
            _isLoading.value = false
        }.addOnFailureListener { error ->
            _isLoading.value = false
            // Handle error
        }
    }


    fun deleteJob(jobId: String) {
        database.child(jobId).removeValue()
            .addOnSuccessListener {
            }
            .addOnFailureListener { error ->
            }
    }

    private fun updateStatusToFirebase(jobList: List<JobModel>) {
        for (jobModel in jobList) {
            database.child(jobModel.jobId.toString()).child("status").setValue(jobModel.status)
                .addOnSuccessListener {
                    // Status updated successfully
                }
                .addOnFailureListener { error ->
                    // Handle error
                }
        }
    }


}
