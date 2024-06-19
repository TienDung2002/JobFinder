package com.example.jobfinder.UI.JobHistory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobHistoryModel
import com.example.jobfinder.Datas.Model.JobHistoryParentModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.FirebaseDatabase

class JobHistoryViewModel : ViewModel() {

    private val _JobHistoryList = MutableLiveData<MutableList<JobHistoryModel>>()
    val JobHistoryList: MutableLiveData<MutableList<JobHistoryModel>> get() = _JobHistoryList

    private val _JobIdList = MutableLiveData<MutableList<JobHistoryParentModel>>()
    val JobIdList: MutableLiveData<MutableList<JobHistoryParentModel>> get() = _JobIdList

    private val database = FirebaseDatabase.getInstance().getReference("NUserJobHistory")
    private val bUserDb = FirebaseDatabase.getInstance().getReference("BUserJobHistory")

    fun pushToFirebaseNUser(jobId: String, uid: String, jobHistoryModel: JobHistoryModel) {
        database.child(uid).child(jobId).setValue(jobHistoryModel)
    }

    fun pushToFirebaseBUser(
        jobId: String,
        buserId: String,
        nUserId: String,
        jobHistoryModel: JobHistoryModel
    ) {
        bUserDb.child(buserId).child(jobId).child(nUserId).setValue(jobHistoryModel)
    }

    fun fetchNUserJobHistory() {
        val uid = GetData.getCurrentUserId()
        if (uid != null) {
            database.child(uid).get().addOnSuccessListener { nuserJobHistorySnapshot ->
                val jobHistoryList: MutableList<JobHistoryModel> = mutableListOf()
                nuserJobHistorySnapshot.children.forEach { nuserJobHistoryData ->
                    val jobHistoryModel = nuserJobHistoryData.getValue(JobHistoryModel::class.java)
                    jobHistoryModel?.let {
                        jobHistoryList.add(it)
                    }
                }
                val sortedJobHistoryList =
                    jobHistoryList.sortedByDescending { GetData.convertStringToDate(it.endDate.toString()) }
                val mutableSortedJobHistoryList = sortedJobHistoryList.toMutableList()
                _JobHistoryList.value = mutableSortedJobHistoryList
            }.addOnFailureListener {
                // Handle failure
            }
        }

    }

    fun fetchBUserJobHistory(jobId: String) {
        val currentBUserId = GetData.getCurrentUserId()
        if (currentBUserId != null) {
            val jobHistoryList: MutableList<JobHistoryModel> = mutableListOf()
            bUserDb.child(currentBUserId).child(jobId).get()
                .addOnSuccessListener { nUserIdSnapshot ->
                    nUserIdSnapshot.children.forEach { bUserJobHistoryData ->
                        val jobHistoryModel =
                            bUserJobHistoryData.getValue(JobHistoryModel::class.java)
                        jobHistoryModel?.let {
                            jobHistoryList.add(it)
                        }
                    }
                    val sortedJobHistoryList =
                        jobHistoryList.sortedByDescending { GetData.convertStringToDate(it.endDate.toString()) }
                    val mutableSortedJobHistoryList = sortedJobHistoryList.toMutableList()
                    _JobHistoryList.value = mutableSortedJobHistoryList
            }.addOnFailureListener {
                // Handle failure
            }
        }
    }

    fun fetchBUserJobHistoryId(){
        val currentBUserId = GetData.getCurrentUserId()
        if (currentBUserId != null) {
            val jobIdList: MutableList<JobHistoryParentModel> = mutableListOf()
//            val jobHistoryList: MutableList<JobHistoryModel> = mutableListOf()
            bUserDb.child(currentBUserId).get().addOnSuccessListener { jobIdSnapshot ->
                jobIdSnapshot.children.forEach {
                    val jobHistoryParentList: MutableList<JobHistoryModel> = mutableListOf()
                    Log.d("LogKeyyyy", it.key.toString())
                    it.children.forEach { bUserJobHistoryData ->
                        Log.d("LogKeyyyy", bUserJobHistoryData.key.toString())
                        val jobHistoryModel =
                            bUserJobHistoryData.getValue(JobHistoryModel::class.java)
                        jobHistoryModel?.let {
//                            jobHistoryList.add(it)
                            jobHistoryParentList.add(it)
                            Log.d("sfsdsdsdf", it.toString())
                        }
                    }
                    val jobHistoryParentModel = JobHistoryParentModel(jobHistoryParentList[0].jobTitle.toString(), jobHistoryParentList)
                    jobIdList.add(jobHistoryParentModel)
                }
//                val sortedJobHistoryList =
//                    jobHistoryList.sortedByDescending { GetData.convertStringToDate(it.endDate.toString()) }
//                val mutableSortedJobHistoryList = sortedJobHistoryList.toMutableList()
//                _JobHistoryList.value = mutableSortedJobHistoryList
                _JobIdList.value = jobIdList.toMutableList()
            }.addOnFailureListener {
                // Handle failure
            }
        }
    }

}