package com.example.jobfinder.UI.Statistical

import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.IncomeModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.FirebaseDatabase

class IncomeViewModel:ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("NUserIncome")

    fun pushIncomeToFirebase(uid:String, income:String, date:String){

        val toFbDate = GetData.formatDateForFirebase(date)

        database.child(uid).child(toFbDate).get().addOnSuccessListener {
            if(it.exists()){
                val incomeModel = it.getValue(IncomeModel::class.java)
                if(incomeModel!= null) {
                    val newIncome = incomeModel.incomeAmount.toString().toFloat() + income.toFloat()
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

}