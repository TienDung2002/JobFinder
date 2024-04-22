package com.example.jobfinder.UI.JobDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.Utils.RetriveImg
import com.example.jobfinder.databinding.ActivitySeekerJobDetailBinding
import java.util.Locale

class SeekerJobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeekerJobDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekerJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val job = intent.getParcelableExtra<JobModel>("job")
        val uid = job?.BUserId

        binding.animationView.visibility = View.VISIBLE
        binding.detailJobScrollView.visibility = View.GONE

        if (job != null) {
            val emp = job.numOfRecruited+"/"+ job.empAmount
            val salaryTxt = "$"+job.salaryPerEmp+resources.getString(R.string.Ji_unit3)
            val shift = job.startHr+" - "+job.endHr
            binding.jobDetailJobTitle.text = job.jobTitle
            binding.jobDetailBuserName.text= job.BUserName?.uppercase(Locale.getDefault())
            binding.jobDetailJobType.text= job.jobType
            binding.jobDetailSalary.text= salaryTxt
            binding.jobDetailEmpAmount.text= emp
            binding.jobDetailStartTime.text= job.startTime
            binding.jobDetailEndTime.text= job.endTime
            binding.jobDetailWorkShift.text= shift
            binding.jobDetailAddress.text= job.address
            binding.jobDetailDes.text= job.jobDes

            RetriveImg.retrieveImage(job.BUserId.toString(), binding.buserLogo)

            binding.detailJobScrollView.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE

            binding.jobDetailBuserName.setOnClickListener {
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }

            binding.buserLogo.setOnClickListener {
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

}