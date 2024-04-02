package com.example.jobfinder.UI.JobDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityJobDetailBinding

class JobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val job = intent.getParcelableExtra<JobModel>("job")
        val uid = job?.BUserId


        GetData.getUserRole { userRole->
            if (userRole == "NUser"){
                binding.detailJobBtnHolder.isEnabled = true
                binding.detailJobBtnHolder.visibility= View.VISIBLE
            }

            if (job != null) {
                val emp = job.numOfRecruited+"/"+ job.empAmount
                binding.jobDetailJobTitle.text = job.jobTitle
                binding.jobDetailBuserName.text= job.BUserName
                binding.jobDetailJobType.text= job.jobType
                binding.jobDetailSalary.text= job.salaryPerEmp
                binding.jobDetailEmpAmount.text= emp
                binding.jobDetailStartTime.text= job.startTime
                binding.jobDetailEndTime.text= job.endTime
                binding.jobDetailWorkShift.text= job.shift?.let { shift(it) }
                binding.jobDetailAddress.text= job.address
                binding.jobDetailDes.text= job.jobDes

                binding.jobDetailBuserName.setOnClickListener {
                    val intent = Intent(this, BUserDetailInfoActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                }
            }
        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

    fun shift(shift: String): String{
        return if(shift == "1"){
            resources.getString(R.string.jdetail_shift_1)
        }else{
            resources.getString(R.string.jdetail_shift_2)
        }
    }
}