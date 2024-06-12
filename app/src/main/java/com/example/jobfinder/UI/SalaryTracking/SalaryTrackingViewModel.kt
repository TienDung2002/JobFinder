package com.example.jobfinder.UI.SalaryTracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.Datas.Model.SalaryModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SalaryTrackingViewModel:ViewModel() {
    private val _CheckInList = MutableLiveData<MutableList<CheckInFromBUserModel>>()
    val CheckInList: MutableLiveData<MutableList<CheckInFromBUserModel>> get() = _CheckInList

    private val _isLoading = MutableLiveData<Boolean>()

    private val database = FirebaseDatabase.getInstance().getReference("NUserCheckIn")
    private val salaryDb = FirebaseDatabase.getInstance().getReference("Salary")

    private val uid = GetData.getCurrentUserId()

    private val _salaryModel = MutableLiveData<SalaryModel?>()
    val salaryModel: MutableLiveData<SalaryModel?> = _salaryModel

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

    fun fetchSalary(jobId: String) {
        if (uid != null) {
            try {
                salaryDb.child(jobId).child(uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val workedDay = snapshot.child("workedDay").getValue(Int::class.java)
                        val totalWorkDay = snapshot.child("totalWorkDay").getValue(Int::class.java)
                        val totalSalary = snapshot.child("totalSalary").getValue(Float::class.java)

                        _salaryModel.value = SalaryModel(totalWorkDay, workedDay, totalSalary)
                    } else {
                        _salaryModel.value = null
                    }
                }.addOnFailureListener { exception ->
                    Log.d("fetchSalary", "Error fetching salary data: $exception")
                    // Xử lý lỗi ở đây, ví dụ: thông báo cho người dùng hoặc gửi lỗi đến hệ thống
                }
            } catch (e: Exception) {
                Log.e("fetchSalary", "Exception occurred: $e")
                // Xử lý ngoại lệ ở đây, ví dụ: thông báo cho người dùng hoặc gửi lỗi đến hệ thống
            }
        }
    }


}