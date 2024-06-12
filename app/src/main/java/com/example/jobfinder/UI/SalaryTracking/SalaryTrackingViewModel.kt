package com.example.jobfinder.UI.SalaryTracking

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.FirebaseDatabase

class SalaryTrackingViewModel:ViewModel() {
    private val _CheckInList = MutableLiveData<MutableList<CheckInFromBUserModel>>()
    val CheckInList: MutableLiveData<MutableList<CheckInFromBUserModel>> get() = _CheckInList

    private val _isLoading = MutableLiveData<Boolean>()

    private val database = FirebaseDatabase.getInstance().getReference("NUserCheckIn")

    private val uid = GetData.getCurrentUserId()

    fun fetchCheckIn(jobId: String) {
        _isLoading.value = true
        if(uid != null) {
            database.child(jobId).get().addOnSuccessListener { dateSnapshot ->
                val CheckInList: MutableList<CheckInFromBUserModel> = mutableListOf()
                dateSnapshot.children.forEach { CheckInSnapshot ->
                    val CheckInModel = CheckInSnapshot.child(uid).getValue(CheckInFromBUserModel::class.java)
                    CheckInModel?.let {
                        if(CheckInModel.checkOutTime!= "") {
                            CheckInList.add(it)
                        }
                    }
                }
                // Sort the list of CheckIns by application date
                val sortedCheckInList =
                    CheckInList.sortedByDescending { GetData.convertStringToDate(it.date.toString()) }
                val mutableSortedCheckInList = sortedCheckInList.toMutableList()
                _CheckInList.value = mutableSortedCheckInList
            }.addOnFailureListener {
                _isLoading.value = false
                // Handle failure
            }
        }
    }
    
}