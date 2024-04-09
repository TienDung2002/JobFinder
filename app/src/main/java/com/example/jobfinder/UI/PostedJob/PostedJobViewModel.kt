package com.example.jobfinder.UI.PostedJob

import android.util.Log
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

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid
    private val database = FirebaseDatabase.getInstance().getReference("Job").child(uid.toString())
    private val userInfoDb = FirebaseDatabase.getInstance().getReference("UserBasicInfo").child(uid.toString())

    fun fetchPostedJobs() {
        _isLoading.value = true
        database.get().addOnSuccessListener { dataSnapshot ->
            val postedJobList: MutableList<JobModel> = mutableListOf()
            userInfoDb.child("name").get().addOnSuccessListener { nameSnapshot ->
                val userName = nameSnapshot.getValue(String::class.java).toString()
                Log.d("BUserName", userName)
                dataSnapshot.children.forEach { jobSnapshot ->
                    val jobModel = jobSnapshot.getValue(JobModel::class.java)
                    jobModel?.let {
                        // Cập nhật tên người dùng cho mỗi công việc
                        it.BUserName = userName
                        Log.d("BUserName2", it.BUserName.toString())
                        // Cập nhật trạng thái dựa trên thời gian bắt đầu và kết thúc
                        it.status = GetData.getStatus(it.startTime.toString(), it.endTime.toString(), it.empAmount.toString(), it.numOfRecruited.toString())
                        // Thêm công việc đã cập nhật vào danh sách
                        postedJobList.add(it)
                    }
                }
                // Sắp xếp danh sách công việc theo thời gian đăng
                val sortedPostedJobList = postedJobList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
                _postedJobList.value = sortedPostedJobList
                // Cập nhật trạng thái vào Firebase
                updateStatusToFirebase(sortedPostedJobList)
                _isLoading.value = false
            }.addOnFailureListener {
                _isLoading.value = false
                // Xử lý lỗi
            }
        }.addOnFailureListener {
            _isLoading.value = false
            // Xử lý lỗi
        }
    }

    fun deleteJob(jobId: String) {
        database.child(jobId).removeValue()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    private fun updateStatusToFirebase(jobList: List<JobModel>) {
        val updatesMap = mutableMapOf<String, Any?>()
        for (jobModel in jobList) {
            updatesMap["/${jobModel.jobId}/buserName"] = jobModel.BUserName
            updatesMap["/${jobModel.jobId}/status"] = jobModel.status
        }
        database.updateChildren(updatesMap)
            .addOnSuccessListener {
                // Tất cả các trạng thái đã được cập nhật thành công
            }
            .addOnFailureListener {
                // Xử lý lỗi
            }
    }

}
