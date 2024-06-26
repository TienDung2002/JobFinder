package com.example.jobfinder.UI.Admin.Statistical

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityAdminStatisticalBinding
import com.example.jobfinder.databinding.ActivityAdminUserManagBinding

class AdminStatisticalActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminStatisticalBinding
    private val userCountViewModel: AdminUserCountViewModel by viewModels()
    private val incomeVM:AdminIncomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatisticalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // nút back về
        binding.backbtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        userCountViewModel.fetchRegisteredBUser()
        userCountViewModel.fetchRegisteredNUser()

        userCountViewModel.registeredUserLists.observe(this) { registeredUserLists ->
            registeredUserLists?.let {
                val bUserList = it.bUserList
                val nUserList = it.nUserList

                // Kiểm tra và xử lý các phần tử trong bUserList
                for (user in bUserList) {
                    Log.d("BUserList", "User: ${user.userType}, Name: ${user.registeredDate} , ${user.amount}")
                }

                // Kiểm tra và xử lý các phần tử trong nUserList
                for (user in nUserList) {
                    Log.d("NUserList", "User: ${user.userType}, Name: ${user.registeredDate} , ${user.amount}")
                }
            }
        }

        incomeVM.fetchIncome()

        incomeVM.incomeList.observe(this){ incomeList->
            for(income in incomeList){
                Log.d("Incomeeee", "${income.incomeAmount} ${income.incomeDate}")
            }
        }

    }
}