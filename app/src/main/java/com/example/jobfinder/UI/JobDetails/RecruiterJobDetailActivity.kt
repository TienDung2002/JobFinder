package com.example.jobfinder.UI.JobDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.databinding.ActivityRecruiterJobDetailBinding

class RecruiterJobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecruiterJobDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val job = intent.getParcelableExtra<JobModel>("job")
        val uid = job?.BUserId

        binding.animationView.visibility = View.VISIBLE
        binding.detailJobScrollView.visibility = View.GONE

        if (job != null) {
            val emp = job.numOfRecruited+"/"+ job.empAmount
            val salaryTxt = "$"+job.salaryPerEmp+resources.getString(R.string.Ji_unit3)
            binding.jobDetailJobTitle.text = job.jobTitle
            binding.jobDetailBuserName.text= job.BUserName
            binding.jobDetailJobType.text= job.jobType
            binding.jobDetailSalary.text= salaryTxt
            binding.jobDetailEmpAmount.text= emp
            binding.jobDetailStartTime.text= job.startTime
            binding.jobDetailEndTime.text= job.endTime
            binding.jobDetailWorkShift.text= job.shift?.let { shift(it) }
            binding.jobDetailAddress.text= job.address
            binding.jobDetailDes.text= job.jobDes

            binding.detailJobScrollView.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE

            binding.jobDetailBuserName.setOnClickListener {
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }

            binding.btnRecLogo.setOnClickListener {
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }
        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun shift(shift: String): String{
        return if(shift == "1"){
            resources.getString(R.string.jdetail_shift_1)
        }else{
            resources.getString(R.string.jdetail_shift_2)
        }
    }
}