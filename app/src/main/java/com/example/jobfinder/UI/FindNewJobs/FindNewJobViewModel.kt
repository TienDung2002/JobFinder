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
    private val _bookmarkStatus = MutableLiveData<Map<String, Boolean>>()
    private val database = FirebaseDatabase.getInstance().getReference("Job")
    var _isLoading = MutableLiveData<Boolean>()


    val jobsListLiveData: LiveData<List<JobModel>> get() = _jobsListLiveData
    val bookmarkStatus: LiveData<Map<String, Boolean>> get() = _bookmarkStatus


    fun getJobsList(): List<JobModel> {
        return JobsList
    }
    fun addJobsToJobsList(JobsData: JobModel) {
        JobsList.add(JobsData)
        _jobsListLiveData.value = JobsList
    }

    fun clearJobsList() {
        JobsList.clear()
        _jobsListLiveData.value = JobsList
    }

    fun updateStatusToFirebase(userId :String,jobList: List<JobModel>) {
        val updatesMap = mutableMapOf<String, Any?>()
        for (jobModel in jobList) {
            updatesMap["/${jobModel.jobId}/buserName"] = jobModel.BUserName
            updatesMap["/${jobModel.jobId}/status"] = jobModel.status
        }
        database.child(userId).updateChildren(updatesMap)
            .addOnSuccessListener {
                // Tất cả các trạng thái đã được cập nhật thành công
            }
            .addOnFailureListener {
                // Xử lý lỗi
            }
    }

    // Cập nhật trạng thái bookmark
    fun updateBookmarkStatus(jobId: String, isBookmarked: Boolean) {
        val newStatus = _bookmarkStatus.value?.toMutableMap() ?: mutableMapOf()
        newStatus[jobId] = isBookmarked
        _bookmarkStatus.value = newStatus
    }
    fun getBookmarkStatus(jobId: String): Boolean {
        return _bookmarkStatus.value?.get(jobId) ?: false
    }
}