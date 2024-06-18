package com.example.jobfinder.UI.JobHistory

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobHistoryModel
import com.google.firebase.database.FirebaseDatabase

class JobHistoryViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("NUserJobHistory")
    private val bUserDb = FirebaseDatabase.getInstance().getReference("BUserJobHistory")

    fun pushToFirebaseNUser(jobId:String, uid:String, jobHistoryModel: JobHistoryModel){
        database.child(jobId).child(uid).setValue(jobHistoryModel)
    }

    fun pushToFirebaseBUser(jobId:String, buserId:String, nUserId:String, jobHistoryModel: JobHistoryModel){
        bUserDb.child(buserId).child(jobId). child(nUserId).setValue(jobHistoryModel)
    }

    fun fetchNUserJobHistory(uid:String){


    }

}