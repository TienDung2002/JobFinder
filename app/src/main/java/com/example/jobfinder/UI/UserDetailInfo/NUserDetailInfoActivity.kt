package com.example.jobfinder.UI.UserDetailInfo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.Datas.Model.SalaryModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.Applicants.ApplicantAdapter
import com.example.jobfinder.UI.Applicants.ApplicantViewModel
import com.example.jobfinder.UI.UsersProfile.ProfileViewModel
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.RetriveImg
import com.example.jobfinder.databinding.ActivityNuserDetailInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NUserDetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuserDetailInfoBinding
    lateinit var viewModel: ProfileViewModel
    private val applicantViewModel: ApplicantViewModel by viewModels()
    private lateinit var adapter: ApplicantAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuserDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance().reference
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val applicant = intent.getParcelableExtra<ApplicantsModel>("nuser_applicant")
        val job =intent.getParcelableExtra<JobModel>("job")

        if (applicant != null && job!= null) {
            val userId = applicant.userId.toString()
            adapter = ApplicantAdapter(mutableListOf(), job, binding.root.context, applicantViewModel)

            val notiRef = FirebaseDatabase.getInstance().getReference("Notifications").child(applicant.userId.toString())
            val appliedJobRef = FirebaseDatabase.getInstance().getReference("AppliedJob").child(applicant.userId.toString()).child(job.jobId.toString())
            val curTime = GetData.getCurrentDateTime()
            userId.let {
                database.child("UserBasicInfo").child(it).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userName = snapshot.child("name").getValue(String::class.java)
                        userName?.let {
                            viewModel.name = it
                            binding.editProfileName.setText(viewModel.name)
                        }
                        val email = snapshot.child("email").getValue(String::class.java)
                        email?.let {
                            viewModel.email = it
                            binding.editProfileEmail.setText(viewModel.email)
                        }
                        val phone = snapshot.child("phone_num").getValue(String::class.java)
                        phone?.let {
                            viewModel.phone = it
                            binding.editProfilePhonenum.setText(viewModel.phone)
                        }
                        val address = snapshot.child("address").getValue(String::class.java)
                        address?.let {
                            viewModel.address = it
                            binding.editProfileAddress.setText(viewModel.address)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
                    }
                })
                database.child("NUserInfo").child(it).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val age = snapshot.child("age").getValue(String::class.java)
                        age?.let {
                            viewModel.age = it
                            binding.editProfileAge.setText(viewModel.age)
                        }
                        val gender = snapshot.child("gender").getValue(String::class.java)
                        gender?.let {
                            viewModel.gender = it
                            binding.editProfileGender.setText(viewModel.gender)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
                    }
                })
                viewModel.userid = it
            }

            binding.applicantDescription.setText(applicant.applicantDes.toString())

            binding.animationView.visibility = View.GONE

            binding.approveBtn.setOnClickListener {
                val jobRef = FirebaseDatabase.getInstance().getReference("Job").child(job.BUserId.toString()).child(job.jobId.toString())
                jobRef.get().addOnSuccessListener { data ->
                    val recruitedAmount =
                        data.child("numOfRecruited").getValue(String::class.java).toString()
                    val empAmount = data.child("empAmount").getValue(String::class.java).toString()
                    if (recruitedAmount.toInt() < empAmount.toInt()) {

                        applicantViewModel.deleteApplicant(
                            job.jobId.toString(),
                            applicant.userId.toString()
                        )

                        val newRecuitedAmount = (recruitedAmount.toInt() + 1).toString()

                        jobRef.child("numOfRecruited").setValue(newRecuitedAmount)

                        appliedJobRef.removeValue()

                        val EmpInJob = ApplicantsModel(
                            applicant.userId,
                            applicant.applicantDes,
                            curTime,
                            applicant.userName
                        )

                        FirebaseDatabase.getInstance().getReference("EmpInJob")
                            .child(job.jobId.toString()).child(applicant.userId.toString())
                            .setValue(EmpInJob)

                        val approvedJob = AppliedJobModel(
                            job.BUserId.toString(),
                            job.jobId.toString(),
                            curTime,
                            job.jobTitle.toString(),
                            job.startHr.toString(),
                            job.endHr.toString(),
                            job.salaryPerEmp.toString(),
                            job.postDate.toString(),
                            job.startTime.toString(),
                            job.endTime.toString()
                        )

                        FirebaseDatabase.getInstance().getReference("ApprovedJob")
                            .child(applicant.userId.toString()).child(job.jobId.toString())
                            .setValue(approvedJob)

                        //salary
                        val totalWorkDay = GetData.countDaysBetweenDates(job.startTime.toString(), job.endTime.toString())
                        val salary = SalaryModel(totalWorkDay, 0, 0f)

                        FirebaseDatabase.getInstance().getReference("Salary")
                            .child(job.jobId.toString()).child(applicant.userId.toString())
                            .setValue(salary)
                        // notification
                        val notiId = notiRef.push().key.toString()
                        val notification = NotificationsRowModel(
                            notiId, job.BUserName.toString(),
                            ContextCompat.getString(
                                binding.root.context,
                                R.string.approve_from
                            ) + " ${job.jobTitle.toString()}", curTime
                        )
                        notiRef.child(notiId).setValue(notification)

                        Toast.makeText(
                            binding.root.context,
                            ContextCompat.getString(binding.root.context, R.string.approve_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            ContextCompat.getString(binding.root.context, R.string.enough_Emp),
                            Toast.LENGTH_SHORT
                        ).show()



                        //khi lấy đủ nhân viên tự xóa hết trong appliedJob của tất cả những ứng viên
                        FirebaseDatabase.getInstance().getReference("AppliedJob").get()
                            .addOnSuccessListener {
                                for (uid in it.children) {

                                    applicantViewModel.deleteApplicant(
                                        job.jobId.toString(),
                                        uid.key.toString()
                                    )
                                    FirebaseDatabase.getInstance().getReference("AppliedJob")
                                        .child(uid.key.toString()).child(job.jobId.toString())
                                        .removeValue()

                                    val notiId =
                                        FirebaseDatabase.getInstance().getReference("Notifications")
                                            .child(uid.key.toString()).push().key.toString()
                                    val notification = NotificationsRowModel(
                                        notiId, job.BUserName.toString(),
                                        ContextCompat.getString(
                                            binding.root.context,
                                            R.string.reject_from
                                        ) + " ${job.jobTitle.toString()}", curTime
                                    )
                                    FirebaseDatabase.getInstance().getReference("Notifications")
                                        .child(uid.key.toString()).child(notiId)
                                        .setValue(notification)

                                }
                            }
                        //xóa hết danh sách ứng viên khi đã full
                        FirebaseDatabase.getInstance().getReference("Applicant")
                            .child(job.jobId.toString()).removeValue()
                    }
                    sendResultAndFinish(job, "true")
                }
            }

            binding.rejectBtn.setOnClickListener {
                applicantViewModel.deleteApplicant(job.jobId.toString() ,applicant.userId.toString())

                // notification
                val notiId = notiRef.push().key.toString()
                val notification = NotificationsRowModel(notiId, job.BUserName.toString(),
                    ContextCompat.getString(binding.root.context, R.string.reject_from) + " ${job.jobTitle.toString()}"
                    ,curTime)
                notiRef.child(notiId).setValue(notification)

                appliedJobRef.removeValue()

                Toast.makeText(
                    binding.root.context,
                    ContextCompat.getString(binding.root.context, R.string.reject_success),
                    Toast.LENGTH_SHORT
                ).show()
                sendResultAndFinish(job, "true")
            }

            binding.backButton.setOnClickListener {
                sendResultAndFinish(job, "false")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        RetriveImg.retrieveImage(viewModel.userid, binding.profileImage)
    }

    private fun sendResultAndFinish(job:JobModel, change:String) {
        val resultIntent = Intent()
        resultIntent.putExtra("jobId", job.jobId.toString())
        resultIntent.putExtra("change", change)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}