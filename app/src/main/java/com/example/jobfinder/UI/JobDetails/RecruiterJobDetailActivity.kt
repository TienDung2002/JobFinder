package com.example.jobfinder.UI.JobDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.databinding.ActivityRecruiterJobDetailBinding

class RecruiterJobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecruiterJobDetailBinding
    private val viewModel: PostedJobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val job = intent.getParcelableExtra<JobModel>("job")

        binding.animationView.visibility = View.VISIBLE
        binding.detailJobScrollView.visibility = View.GONE

        if (job != null) {
            val emp = job.numOfRecruited+"/"+ job.empAmount
            val salaryTxt = "$"+job.salaryPerEmp+resources.getString(R.string.Ji_unit3)
            val shift = job.startHr+"-"+job.endHr
            binding.jobDetailJobTitle.text = job.jobTitle
            binding.jobDetailJobType.text= job.jobType
            binding.jobDetailSalary.text= salaryTxt
            binding.jobDetailEmpAmount.text= emp
            binding.jobDetailStartTime.text= job.startTime
            binding.jobDetailEndTime.text= job.endTime
            binding.jobDetailWorkShift.text= shift
            binding.jobDetailAddress.text= job.address
            binding.jobDetailDes.text= job.jobDes

            binding.detailJobScrollView.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE

            // Thay đổi phần xóa công việc để gửi kết quả trở lại
            binding.deleteBtn.setOnClickListener {
                viewModel.deleteJob(job.jobId.toString())
                Toast.makeText(binding.root.context, ContextCompat.getString(binding.root.context, R.string.deleted_job), Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            binding.appliedListBtn.setOnClickListener {
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", job.jobId)
                startActivity(intent)
            }
        }

        binding.backButton.setOnClickListener {
            setResult(RESULT_OK)
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