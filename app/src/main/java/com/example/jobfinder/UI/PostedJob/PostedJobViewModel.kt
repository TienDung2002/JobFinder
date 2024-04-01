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

    fun fetchPostedJobs() {
        _isLoading.value = true
        database.get().addOnSuccessListener { dataSnapshot ->
            val postedJobList: MutableList<JobModel> = mutableListOf()
            for (jobSnapshot in dataSnapshot.children) {
                val jobModel = jobSnapshot.getValue(JobModel::class.java)
                jobModel?.let { postedJobList.add(it) }
            }
            val sortedPostedJobList = postedJobList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
            _postedJobList.value = sortedPostedJobList
            _isLoading.value = false
        }.addOnFailureListener { error ->
            _isLoading.value = false
            // Handle error
        }
    }
}
