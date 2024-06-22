package com.example.jobfinder.UI.Statistical

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.IncomeModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.FirebaseDatabase

class IncomeViewModel:ViewModel() {
    var userRole: String = ""
    private val _incomeList = MutableLiveData<MutableList<IncomeModel>>()
    val incomeList: MutableLiveData<MutableList<IncomeModel>> get() = _incomeList

    private val database = FirebaseDatabase.getInstance().getReference("NUserIncome")

    fun pushIncomeToFirebase(uid:String, income:String, date:String){

        val toFbDate = GetData.formatDateForFirebase(date)

        database.child(uid).child(toFbDate).get().addOnSuccessListener {
            if(it.exists()){
                val incomeModel = it.getValue(IncomeModel::class.java)
                if(incomeModel!= null) {
                    val newIncome = incomeModel.incomeAmount.toString().toInt() + income.toInt()
                    val update = hashMapOf<String, Any>(
                        "incomeAmount" to newIncome.toString()
                    )
                    database.child(uid).child(toFbDate).updateChildren(update)
                }
            }else{
                val incomeModel = IncomeModel(date, income)
                database.child(uid).child(toFbDate).setValue(incomeModel)
            }
        }
    }
    
    fun fetchIncome(uid: String){
        database.child(uid).get().addOnSuccessListener { 
            if(it.exists()){
                val incomeList: MutableList<IncomeModel> = mutableListOf()
                it.children.forEach { incomeSnapshot->
                    val incomeModel = incomeSnapshot.getValue(IncomeModel::class.java)
                    incomeModel?.let{
                        incomeList.add(incomeModel)
                    }
                }
                _incomeList.value = incomeList
            }
        }
    }

}