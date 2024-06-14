package com.example.jobfinder.UI.SalaryTracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.Datas.Model.SalaryModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.CheckTime
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivitySalaryTrackingBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.roundToInt

class SalaryTrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalaryTrackingBinding
    private val viewModel: SalaryTrackingViewModel by viewModels()
    private var isCliked = false
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

        val applied_job = intent.getParcelableExtra<AppliedJobModel>("applied_job")

        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("VND")

        val today = GetData.getCurrentDateTime()
        val todayDate = GetData.getDateFromString(today)


        if(applied_job!= null ){

            val adapter = SalaryTrackingAdapter(binding.root.context, mutableListOf(), applied_job)
            binding.recyclerSalaryTrackingList.adapter = adapter
            binding.recyclerSalaryTrackingList.layoutManager = LinearLayoutManager(this)

            viewModel.CheckInList.observe(this) { updatedList ->
                adapter.updateData(updatedList)
                checkEmptyAdapter(updatedList)
            }

            viewModel.salaryModel.observe(this) { salaryModel ->
                if (!isFinishing && !isDestroyed) {
                    if(salaryModel!= null) {
                        binding.workedDay.text =
                            "${getText(R.string.worked_day)}: ${salaryModel.workedDay.toString()}/${salaryModel.totalWorkDay.toString()}"
                        val totalSalaryFormatted = format.format(salaryModel.totalSalary)
                        binding.totalSalary.text = "${getText(R.string.total_salary)}: $totalSalaryFormatted"
                    }
                    else {
                        // Xử lý khi salaryModel null
                        binding.workedDay.text = "${getText(R.string.worked_day)}: -/-"
                        binding.totalSalary.text = "${getText(R.string.total_salary)}: -"
                    }
                }
            }

            viewModel.jobModel.observe(this){jobModel ->
                if(jobModel!= null) {
                    if (CheckTime.isDateAfter(todayDate, jobModel.endTime.toString())){
                        binding.confirmEndJobHolder.visibility=View.VISIBLE
                    }
                }
            }

            viewModel.fetchCheckIn(applied_job.jobId.toString())

            viewModel.fetchSalary(applied_job.jobId.toString())

            viewModel.fetchJob(applied_job.jobId.toString(), applied_job.buserId.toString())

            binding.cfEndJobBtn.setOnClickListener {
                Log.d("clicked cfEndJobBtn", "Clicked")
                // xử lí hoàn tiền


            }
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

}