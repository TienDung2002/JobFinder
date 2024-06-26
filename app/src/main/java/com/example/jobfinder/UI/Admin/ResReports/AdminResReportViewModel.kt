package com.example.jobfinder.UI.Admin.ResReports

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.SupportUser
import com.google.firebase.database.FirebaseDatabase

class AdminResReportViewModel: ViewModel() {

    private val _ReportList = MutableLiveData<MutableList<SupportUser>>()
    val reportList: MutableLiveData<MutableList<SupportUser>> get() = _ReportList

    private val _FeedbackList = MutableLiveData<MutableList<SupportUser>>()
    val feedbackList: MutableLiveData<MutableList<SupportUser>> get() = _FeedbackList

    private val _TechList = MutableLiveData<MutableList<SupportUser>>()
    val techList: MutableLiveData<MutableList<SupportUser>> get() = _TechList

    private val database = FirebaseDatabase.getInstance().getReference("AdminRef").child("Report")

    fun fetchReport(){
        database.get().addOnSuccessListener {
            if(it.exists()){
                val reportList: MutableList<SupportUser> = mutableListOf()
                it.children.forEach { reportSnapshot->
                    val reportModel = reportSnapshot.getValue(SupportUser::class.java)
                    reportModel?.let{
                        if (reportModel.supportName == "report"){ reportList.add(reportModel)}
                    }
                }
                _ReportList.value = reportList
            }else{
                _ReportList.value = mutableListOf()
            }
        }
    }

    fun fetchFeedback(){
        database.get().addOnSuccessListener {
            if(it.exists()){
                val reportList: MutableList<SupportUser> = mutableListOf()
                it.children.forEach { reportSnapshot->
                    val reportModel = reportSnapshot.getValue(SupportUser::class.java)
                    reportModel?.let{
                        if (reportModel.supportName == "feedback"){ reportList.add(reportModel)}
                    }
                }
                _FeedbackList.value = reportList
            }else{
                _FeedbackList.value = mutableListOf()
            }
        }
    }

    fun fetchTech(){
        database.get().addOnSuccessListener {
            if(it.exists()){
                val reportList: MutableList<SupportUser> = mutableListOf()
                it.children.forEach { reportSnapshot->
                    val reportModel = reportSnapshot.getValue(SupportUser::class.java)
                    reportModel?.let{
                        if (reportModel.supportName == "technical"){ reportList.add(reportModel)}
                    }
                }
                _TechList.value = reportList
            }else{
                _TechList.value = mutableListOf()
            }
        }
    }
}