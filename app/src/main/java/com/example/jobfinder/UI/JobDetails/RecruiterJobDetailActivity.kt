package com.example.jobfinder.UI.JobDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.PostedJob.PostedJobActivity
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.databinding.ActivityRecruiterJobDetailBinding

class RecruiterJobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecruiterJobDetailBinding
    private val viewModel: PostedJobViewModel by viewModels()
    private val REQUEST_CODE_POSTED_JOB = 1001

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_POSTED_JOB && resultCode == Activity.RESULT_OK) {
            viewModel.fetchPostedJobs()
        }
    }

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

            binding.deleteBtn.setOnClickListener {
                viewModel.deleteJob(job.jobId.toString())
                Toast.makeText(binding.root.context,
                    ContextCompat.getString(binding.root.context, R.string.deleted_job), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, PostedJobActivity::class.java)

                startActivityForResult(intent, REQUEST_CODE_POSTED_JOB)
                finish()
            }

            binding.appliedListBtn.setOnClickListener {
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", job.jobId)
                startActivity(intent)
            }
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, PostedJobActivity::class.java)
            startActivity(intent)
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