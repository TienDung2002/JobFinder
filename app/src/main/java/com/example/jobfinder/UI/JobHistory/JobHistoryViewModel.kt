package com.example.jobfinder.UI.JobHistory

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobHistoryModel
import com.google.firebase.database.FirebaseDatabase

class JobHistoryViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("NUserJobHistory")

    fun pushToFirebase(jobId:String, uid:String, jobHistoryModel: JobHistoryModel){
        database.child(jobId).child(uid).setValue(jobHistoryModel)
    }
}