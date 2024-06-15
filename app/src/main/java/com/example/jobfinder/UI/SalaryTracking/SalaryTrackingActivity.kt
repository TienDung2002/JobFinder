package com.example.jobfinder.UI.SalaryTracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.BUserJobHistoryModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.Datas.Model.JobHistoryModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.CheckIn.CheckInViewModel
import com.example.jobfinder.UI.JobHistory.JobHistoryViewModel
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.Utils.CheckTime
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivitySalaryTrackingBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.Currency

class SalaryTrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalaryTrackingBinding
    private val viewModel: SalaryTrackingViewModel by viewModels()
    private val checkInViewModel: CheckInViewModel by viewModels()
    private val jobHistoryViewModel: JobHistoryViewModel by viewModels()
    private val jobViewModel: PostedJobViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back bằng nút trên màn hình
        binding.backButton.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val approved_job = intent.getParcelableExtra<AppliedJobModel>("approved_job")

        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("VND")

        val today = GetData.getCurrentDateTime()
        val todayDate = GetData.getDateFromString(today)
        val currentTime = GetData.getTimeFromString(today)

        val uid = GetData.getCurrentUserId()


        if(approved_job!= null && uid != null){

            val adapter = SalaryTrackingAdapter(binding.root.context, mutableListOf(), approved_job)
            binding.recyclerSalaryTrackingList.adapter = adapter
            binding.recyclerSalaryTrackingList.layoutManager = LinearLayoutManager(this)

            viewModel.CheckInList.observe(this) { updatedList ->
                adapter.updateData(updatedList)
                checkEmptyAdapter(updatedList)
            }

            viewModel.salaryModel.observe(this) { salaryModel ->
                if(salaryModel!= null) {
                    binding.workedDay.text =
                        "${getText(R.string.worked_day)}: ${salaryModel.workedDay.toString()}/${salaryModel.totalWorkDay.toString()}"
                    val totalSalaryFormatted = format.format(salaryModel.totalSalary)
                    binding.totalSalary.text = "${getText(R.string.total_salary)}: $totalSalaryFormatted"
                }
            }

            viewModel.jobModel.observe(this){jobModel ->
                if(jobModel!= null) {
                    if( CheckTime.areDatesEqual( todayDate, jobModel.endTime.toString())){
                        if(CheckTime.calculateMinuteDiff(approved_job.endHr.toString(),currentTime) >0){
                            binding.confirmEndJobHolder.visibility=View.VISIBLE
                            binding.cfEndJobBtn.isClickable =true
                            binding.cfEndJobBtn.setOnClickListener {
                                // xử lí hoàn tiền và thêm vào job history
                                Log.d("sdkjfbdskjfbdskjfkjdsjkf", "clicked")
                                endJobHandle(approved_job, uid.toString(), todayDate)
                            }
                        }
                    }
                    if(!CheckTime.areDatesEqual( todayDate, jobModel.endTime.toString())) {
                        if (CheckTime.isDateAfter(todayDate, jobModel.endTime.toString())) {
                            binding.confirmEndJobHolder.visibility = View.VISIBLE
                            binding.cfEndJobBtn.isClickable =true
                            binding.cfEndJobBtn.setOnClickListener {
                                // xử lí hoàn tiền và thêm vào job history
                                Log.d("sdkjfbdskjfbdskjfkjdsjkf", "clicked2")
                                endJobHandle(approved_job, uid.toString(), todayDate)
                            }
                        }
                    }
                }
            }

            viewModel.fetchCheckIn(approved_job.jobId.toString())

            viewModel.fetchSalary(approved_job.jobId.toString())

            viewModel.fetchJob(approved_job.jobId.toString(), approved_job.buserId.toString())

        }
    }

    // back bằng nút hoặc vuốt trên thiết bị
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Khởi tạo Intent để quay lại HomeActivity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun checkEmptyAdapter(list: MutableList<CheckInFromBUserModel>) {
        if (list.isEmpty()) {
            binding.noSalaryTracking.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
            binding.titleHolder.visibility = View.GONE
        } else {
            binding.noSalaryTracking.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }

    private fun endJobHandle(approved_job: AppliedJobModel, uid: String, todayDate: String) {
        val jobRef = FirebaseDatabase.getInstance().getReference("Job")
            .child(approved_job.buserId.toString()).child(approved_job.jobId.toString())

        val salaryRef = FirebaseDatabase.getInstance().getReference("Salary")
            .child(approved_job.jobId.toString()).child(uid)

        salaryRef.get().addOnSuccessListener { salarySnapshot ->
            if (salarySnapshot.exists()) {
                val nuserTotalSalary = salarySnapshot.child("totalSalary").getValue(Float::class.java)
                Log.d("endJobHandle", "NUser total salary: $nuserTotalSalary")

                // update wallet amount
                jobViewModel.addWalletAmount(uid, nuserTotalSalary.toString().toFloat())

                jobRef.get().addOnSuccessListener { jobSnapshot ->
                    if (jobSnapshot.exists()) {
                        val jobModel = jobSnapshot.getValue(JobModel::class.java)
                        if (jobModel != null) {
                            Log.d("endJobHandle", "Job model retrieved: $jobModel")

                            val newJobTotalSalary = jobModel.totalSalary.toString().toFloat() - nuserTotalSalary.toString().toFloat()
                            val newJobRecruitedEmp = jobModel.numOfRecruited.toString().toInt() - 1

                            val updateJob = hashMapOf<String, Any>(
                                "numOfRecruited" to newJobRecruitedEmp.toString(),
                                "totalSalary" to newJobTotalSalary.toString()
                            )

                            jobRef.updateChildren(updateJob).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("endJobHandle", "Job updated successfully")

                                    // push lên NUserJobHistory
                                    val nUserJobHistoryModel = JobHistoryModel(
                                        jobModel.jobId, jobModel.jobTitle, todayDate,
                                        jobModel.jobType, jobModel.BUserId, "", "", uid
                                    )
                                    jobHistoryViewModel.pushToFirebase(jobModel.jobId.toString(), uid, nUserJobHistoryModel)

                                    // push lên BUserJobHistory
                                    val bUserJobHistoryRef = FirebaseDatabase.getInstance().getReference("BUserJobHistory")
                                        .child(jobModel.BUserId.toString()).child(jobModel.jobId.toString()).child(uid)

                                    val bUserJobHistoryModel = BUserJobHistoryModel(jobModel.jobId, jobModel.BUserId, uid)

                                    bUserJobHistoryRef.setValue(bUserJobHistoryModel)

                                    // xóa trong approvedJob
                                    checkInViewModel.removeApprovedJob(jobModel.jobId.toString(), uid)

                                    // xóa trong salary
                                    salaryRef.removeValue()

                                    if (newJobRecruitedEmp == 0) {
                                        // khi nuser cuối xác nhận kết thúc việc để nhận lương sẽ xóa job

                                        // hoàn tiền vào ví buser
                                        jobViewModel.addWalletAmount(jobModel.BUserId.toString(), newJobTotalSalary)

                                        //xóa trong check in và buser check in

                                        // xóa job
                                        jobRef.removeValue()
                                    }

                                    val resultIntent = Intent()
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()

                                } else {
                                    Log.e("endJobHandle", "Failed to update job")
                                }
                            }
                        } else {
                            Log.e("endJobHandle", "Job model is null")
                        }
                    } else {
                        Log.e("endJobHandle", "Job snapshot does not exist")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("endJobHandle", "Failed to get job snapshot", exception)
                }
            } else {
                Log.e("endJobHandle", "Salary snapshot does not exist")
            }
        }.addOnFailureListener { exception ->
            Log.e("endJobHandle", "Failed to get salary snapshot", exception)
        }
    }


}