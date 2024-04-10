package com.example.jobfinder.UI.FindNewJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FindNewJobViewModel : ViewModel() {
    private val _postedJobList = MutableLiveData<List<JobModel>>()
    val postedJobList: LiveData<List<JobModel>> get() = _postedJobList
    val _isLoading = MutableLiveData<Boolean>()
    private val database = FirebaseDatabase.getInstance().getReference("Job")

    fun fetchJobs() {
        _isLoading.value = true
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postedJobList: MutableList<JobModel> = mutableListOf()
                for (userSnapshot in dataSnapshot.children) {
                    for (jobSnapshot in userSnapshot.children) {
                        val jobModel = jobSnapshot.getValue(JobModel::class.java)
                        jobModel?.let {
                            postedJobList.add(it)
                        }
                    }
                }
                // Sắp xếp danh sách công việc theo thời gian đăng
                val sortedPostedJobList =
                    postedJobList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
                _postedJobList.value = sortedPostedJobList
                _isLoading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _isLoading.value = false
                // Xử lý lỗi
            }
        })
    }

}
