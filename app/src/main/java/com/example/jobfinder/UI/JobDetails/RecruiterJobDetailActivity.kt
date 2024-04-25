package com.example.jobfinder.UI.JobDetails

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.Applicants.ActivityApplicantsList
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.RetriveImg
import com.example.jobfinder.databinding.ActivityRecruiterJobDetailBinding
import com.google.firebase.database.*

class RecruiterJobDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecruiterJobDetailBinding
    private val viewModel: PostedJobViewModel by viewModels()

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val job = intent.getParcelableExtra<JobModel>("job")

        binding.animationView.visibility = View.VISIBLE
        binding.detailJobScrollView.visibility = View.GONE

        if (job != null) {
            if(job.status.toString()== "closed"){
                binding.applicantBtn.visibility = View.GONE
            }
            if(job.status.toString()== "working"){
                binding.detailJobBtnHolder.visibility = View.GONE
            }
            val emp = "${job.numOfRecruited}/${job.empAmount}"
            val salaryTxt = "$${job.salaryPerEmp}${resources.getString(R.string.Ji_unit3)}"
            val shift = "${job.startHr}-${job.endHr}"
            binding.jobDetailJobTitle.text = job.jobTitle
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

            binding.deleteBtn.setOnClickListener {
                if (job.status.toString() == "recruiting") {
                    amountWorking(job)
                }
                viewModel.deleteJob(job.jobId.toString())
                Toast.makeText(this@RecruiterJobDetailActivity, getString(R.string.deleted_job), Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            binding.appliedListBtn.setOnClickListener {
                val intent = Intent(this, ActivityApplicantsList::class.java)
                intent.putExtra("job", job)
                startActivity(intent)
            }

            binding.buserLogo.setOnClickListener{
                val intent = Intent(this, BUserDetailInfoActivity::class.java)
                intent.putExtra("uid", job.BUserId.toString())
                startActivity(intent)
            }
        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun amountWorking(job: JobModel){
        val walletAmountRef =
            FirebaseDatabase.getInstance().getReference("WalletAmount")
                .child(job.BUserId.toString()).child("amount")
        walletAmountRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val walletAmountString = snapshot.getValue(String::class.java)
                    val walletAmount = walletAmountString?.toFloatOrNull() ?: 0f
                    val newWalletAmount = walletAmount + job.totalSalary.toString().toFloat()
                    //refund to wallet
                    walletAmountRef.setValue(newWalletAmount.toString())

                    // notification
                    val date = GetData.getCurrentDateTime()
                    val notiId = FirebaseDatabase.getInstance()
                        .getReference("Notifications")
                        .child(job.BUserId.toString()).push().key.toString()
                    val notificationsRowModel = NotificationsRowModel(
                        notiId,
                        "Admin",
                        "${getString(R.string.refund)}.\n" +
                                "${getString(R.string.from_job)} ${job.jobTitle}.\n" +
                                "+$${job.totalSalary} ${getString(R.string.to_wallet)}",
                        date
                    )
                    FirebaseDatabase.getInstance()
                        .getReference("Notifications")
                        .child(job.BUserId.toString())
                        .child(notiId)
                        .setValue(notificationsRowModel)
                } else {
                    // Handle case when wallet data does not exist
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

}
