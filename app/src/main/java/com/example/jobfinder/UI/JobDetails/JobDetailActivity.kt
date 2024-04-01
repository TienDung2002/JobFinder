package com.example.jobfinder.UI.JobDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityJobDetailBinding

class JobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val job = intent.getParcelableExtra<JobModel>("job")
        GetData.getUserRole { userRole->
            if (userRole == "NUser"){
                binding.detailJobBtnHolder.isEnabled = true
                binding.detailJobBtnHolder.visibility= View.VISIBLE
            }

            if (job != null) {
                val emp = job.numOfRecruited+"/"+ job.empAmount
                binding.jobDetailJobTitle.text = job.jobTitle
                binding.jobDetailBuserName.text= job.BUserName
                binding.jobDetailSalary.text= job.salaryPerEmp
                binding.jobDetailEmpAmount.text= emp
                binding.jobDetailStartTime.text= job.startTime
                binding.jobDetailEndTime.text= job.endTime
                binding.jobDetailWorkShift.text= job.shift
                binding.jobDetailAddress.text= job.address
                binding.jobDetailDes.text= job.jobDes
            }else{
                Log.d("null job", "null jobbbbbb")
            }
        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


    }
}