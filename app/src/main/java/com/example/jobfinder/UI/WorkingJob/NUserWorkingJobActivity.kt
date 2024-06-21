package com.example.jobfinder.UI.WorkingJob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.CheckIn.CheckInViewModel
import com.example.jobfinder.UI.Home.HomeFragmentBuser
import com.example.jobfinder.UI.Home.HomeFragmentNuser
import com.example.jobfinder.UI.JobHistory.JobHistoryFragment
import com.example.jobfinder.UI.Notifications.NotificationsFragment
import com.example.jobfinder.UI.SalaryTracking.SalaryTrackingActivity
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityNuserWorkingJobBinding
import com.example.jobfinder.databinding.ActivityWorkingJobBinding

class NUserWorkingJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuserWorkingJobBinding
    private var workingJob = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuserWorkingJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            sendResultAndFinish()
        }

        FragmentHelper.replaceFragment(supportFragmentManager, binding.jobManagementActivityFrameLayout, NUserWorkingJobFragment(binding.animationView))

        binding.jobHistoryTitleHolder.setOnClickListener{
            if (!isCurrentFragment(R.id.job_history_title)) {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.jobManagementActivityFrameLayout, JobHistoryFragment(binding.animationView))
                workingJob = false
                changeTitleHolderColor(workingJob)
            }
        }

        binding.workingJobTitleHolder.setOnClickListener {
            if (!isCurrentFragment(R.id.working_job_title)) {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.jobManagementActivityFrameLayout, NUserWorkingJobFragment(binding.animationView))
                workingJob = true
                changeTitleHolderColor(workingJob)
            }
        }


    }

    private fun sendResultAndFinish() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        sendResultAndFinish()
    }

    private fun changeTitleHolderColor(workingJob:Boolean){
        if(workingJob){
            binding.workingJobTitleHolder.setBackgroundColor(getColor(R.color.home_bg))
            binding.workingJobTitle.setTextColor(getColor(R.color.primary_color1))
            binding.workingJobTitle.setTypeface(null, Typeface.BOLD)

            binding.jobHistoryTitleHolder.setBackgroundColor(getColor(R.color.white))
            binding.jobHistoryTitle.setTextColor(getColor(R.color.black))
            binding.jobHistoryTitle.setTypeface(null, Typeface.NORMAL)

        }else{
            binding.jobHistoryTitleHolder.setBackgroundColor(getColor(R.color.home_bg))
            binding.jobHistoryTitle.setTextColor(getColor(R.color.primary_color1))
            binding.jobHistoryTitle.setTypeface(null, Typeface.BOLD)

            binding.workingJobTitleHolder.setBackgroundColor(getColor(R.color.white))
            binding.workingJobTitle.setTextColor(getColor(R.color.black))
            binding.workingJobTitle.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun isCurrentFragment(itemId: Int): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.job_management_activity_frame_layout)
        return when (itemId) {
            R.id.working_job_title -> currentFragment is NUserWorkingJobFragment
            R.id.job_history_title -> currentFragment is JobHistoryFragment
            else -> false
        }
    }
}