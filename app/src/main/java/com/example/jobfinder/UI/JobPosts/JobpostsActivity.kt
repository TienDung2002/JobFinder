package com.example.jobfinder.UI.JobPosts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.Datas.Model.walletAmountModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.Calendar
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityJobpostsBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import java.text.NumberFormat
import java.util.Currency


class JobpostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobpostsBinding
    private lateinit var auth: FirebaseAuth
    private var jobType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobpostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()

        setupSpinner()

        binding.postJobTitle.isClickable = true
        binding.postJobStartTime.isClickable = true
        binding.postJobEndTime.isClickable = true
        binding.postJobEmpAmount.isClickable = true
        binding.postJobSalary.isClickable = true
        binding.postJobAddress.isClickable = true
        binding.postJobDes.isClickable = true
        binding.postJobBtn.isClickable = true
        binding.postJobStartHr.isClickable= true
        binding.postJobEndHr.isClickable= true

        //back btn
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        //start time shift
        binding.postJobStartHr.setOnClickListener{
            Calendar.showTimePickerDialog(binding.root.context, binding.postJobStartHr)
        }

        //end time shift
        binding.postJobEndHr.setOnClickListener{
            Calendar.showTimePickerDialog(binding.root.context, binding.postJobEndHr)
        }

        //start time
        binding.postJobStartTime.setOnClickListener{
            Calendar.setupDatePicker(binding.root.context, binding.postJobStartTime)
        }

        //end time
        binding.postJobEndTime.setOnClickListener {
            Calendar.setupDatePicker(binding.root.context, binding.postJobEndTime)
        }

        //button
        binding.postJobBtn.setOnClickListener {
            val title = binding.postJobTitle.text.toString()
            val workStartTime = binding.postJobStartHr.text.toString()
            val workEndTime = binding.postJobEndHr.text.toString()
            val startTime = binding.postJobStartTime.text.toString()
            val endTime = binding.postJobEndTime.text.toString()
            val empAmount = binding.postJobEmpAmount.text.toString()
            val salary = binding.postJobSalary.text.toString()
            val address = binding.postJobAddress.text.toString()
            val jobDes= binding.postJobDes.text.toString()
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance("VND")

            //field check
            val isValidStartTime = VerifyField.isEmpty(startTime.trim())
            val isValidEndTime = VerifyField.isEmpty(endTime.trim())
            val isValidTitle = VerifyField.isEmpty(title.trim())
            val isValidEmpAmount =  VerifyField.maxEmpAmount(empAmount)
            val isValidSalary =  VerifyField.isEmpty(salary.trim())
            val isValidAddress = VerifyField.isEmpty(address.trim())
            val isValidJobDes = VerifyField.isEmpty(jobDes.trim())
            val isValidWorkStartTime = VerifyField.isEmpty(workStartTime.trim())
            val isValidWorkEndTime = VerifyField.isEmpty(workEndTime.trim())
            val isValidWorkDate = GetData.compareDates(startTime, endTime)
            val isValidWorkShift = GetData.isTimeBeforeOneHour(workStartTime, workEndTime)

            if(!isValidWorkDate && isValidStartTime && isValidEndTime){
                Toast.makeText(binding.root.context, getString(R.string.end_time_be4_start),Toast.LENGTH_SHORT).show()
            }

            if(!isValidWorkShift && isValidWorkStartTime && isValidWorkEndTime){
                Toast.makeText(binding.root.context, getString(R.string.end_shift_time_be4_start),Toast.LENGTH_SHORT).show()
            }
            binding.postJobTitle.error = if (isValidTitle) null else getString(R.string.no_post_job_title)
            binding.postJobEmpAmount.error = if(isValidEmpAmount) null else getString(R.string.no_emp_amount)
            binding.postJobSalary.error = if(isValidSalary) null else getString(R.string.no_salary)
            binding.postJobAddress.error = if(isValidAddress) null else getString(R.string.no_address)
            binding.postJobDes.error = if(isValidJobDes) null else getString(R.string.no_job_des)
            binding.postJobStartTime.error= if(isValidStartTime) null else getString(R.string.no_start_time)
            binding.postJobEndTime.error= if(isValidEndTime) null else getString(R.string.no_end_time)
            binding.postJobStartHr.error= if(isValidWorkStartTime) null else getString(R.string.shift_start_blank)
            binding.postJobEndHr.error= if(isValidWorkEndTime) null else getString(R.string.shift_end_blank)

            if(isValidTitle && isValidAddress && isValidEmpAmount && isValidSalary && isValidJobDes
                && isValidStartTime && isValidEndTime && isValidWorkDate && isValidWorkShift && isValidWorkStartTime && isValidWorkEndTime){

                binding.postJobTitle.isClickable = false
                binding.postJobStartTime.isClickable = false
                binding.postJobEndTime.isClickable = false
                binding.postJobEmpAmount.isClickable = false
                binding.postJobSalary.isClickable = false
                binding.postJobAddress.isClickable = false
                binding.postJobDes.isClickable = false
                binding.postJobBtn.isClickable = false
                binding.postJobStartHr.isClickable= false
                binding.postJobEndHr.isClickable= false

                val uid = auth.currentUser?.uid

                val jobId= FirebaseDatabase.getInstance().getReference("Job").child(uid.toString()).push().key
                val totalWorkDay = GetData.countDaysBetweenDates(startTime, endTime)
                val hrWordPerDay = GetData.calculateHourDifference(workStartTime, workEndTime)
                val totalWorkHour= totalWorkDay.toFloat() * hrWordPerDay
                val oneEmpSalary = GetData.multiplyStrings(totalWorkHour.toString(), salary)
                val totalSalary = GetData.multiplyStrings(empAmount, oneEmpSalary)
                val date = GetData.getCurrentDateTime()

                FirebaseDatabase.getInstance().getReference("UserBasicInfo").child(uid.toString()).get().addOnSuccessListener { data ->
                    if(data.exists()) {
                        val bUserName = data.child("name").getValue(String::class.java).toString()
                        val walletAmountRef = FirebaseDatabase.getInstance().getReference("WalletAmount").child(uid.toString())
                        walletAmountRef.get().addOnSuccessListener { walletData ->
                            if(walletData.exists()) {
                                val walletAmount =
                                    walletData.child("amount").getValue(String::class.java)
                                        .toString()

                                if(GetData.compareFloatStrings(walletAmount, totalSalary)) {
                                    val newJob = JobModel(
                                        jobId,
                                        title,
                                        startTime,
                                        endTime,
                                        empAmount,
                                        salary,
                                        address,
                                        jobDes,
                                        totalSalary,
                                        date,
                                        "0",
                                        bUserName,
                                        jobType,
                                        uid,
                                        "recruiting",
                                        workStartTime,
                                        workEndTime
                                    )

                                    //add to firebase
                                    FirebaseDatabase
                                        .getInstance()
                                        .getReference("Job")
                                        .child(uid.toString())
                                        .child(jobId.toString())
                                        .setValue(newJob)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                val newWalletAmount = walletAmountModel((walletAmount.toFloat()-totalSalary.toFloat()).toString())
                                                walletAmountRef.setValue(newWalletAmount)
                                                val notiId = FirebaseDatabase
                                                    .getInstance()
                                                    .getReference("Notifications")
                                                    .child(uid.toString()).push().key.toString()
                                                val convertSalaryVnd = format.format(totalSalary.toDouble())
                                                val notificationsRowModel = NotificationsRowModel(
                                                    notiId,
                                                    "Admin",
                                                    "${getString(R.string.post_job_success)}.\n" +
                                                            "${getString(R.string.post_job_title)}: $title.\n" +
                                                            "-$convertSalaryVnd ${getString(R.string.from_wallet)}",
                                                    date
                                                )
                                                FirebaseDatabase.getInstance()
                                                    .getReference("Notifications")
                                                    .child(uid.toString())
                                                    .child(notiId)
                                                    .setValue(notificationsRowModel)
                                                Toast.makeText(
                                                    binding.root.context,
                                                    getString(R.string.post_job_success),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                // back home page
                                                val resultIntent = Intent()
                                                setResult(Activity.RESULT_OK, resultIntent)
                                                finish()
                                            }
                                        }
                                }else{
                                    Toast.makeText(binding.root.context, getString(R.string.not_enough_money),Toast.LENGTH_SHORT).show()
                                    binding.postJobBtn.isClickable = true
                                }
                            }
                        }
                    }
                }
            }else{
                checkToAutoFocus(isValidTitle,isValidStartTime, isValidEndTime , isValidEmpAmount, isValidSalary, isValidAddress, isValidJobDes)
            }

        }

    }

    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<TextInputEditText>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.postJobTitle)
                    1 -> invalidFields.add(binding.postJobStartTime)
                    2 -> invalidFields.add(binding.postJobEndTime)
                    3 -> invalidFields.add(binding.postJobEmpAmount)
                    4 -> invalidFields.add(binding.postJobSalary)
                    5 -> invalidFields.add(binding.postJobAddress)
                    6 -> invalidFields.add(binding.postJobDes)
                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }

    private fun setupSpinner() {
        val spinner = binding.jobTypeSpinner
        val jobTypesArray = resources.getStringArray(R.array.job_types_array)
        val spinnerAdapter = JobTypeSpinner(binding.root.context, jobTypesArray)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jobType = parent?.getItemAtPosition(position).toString()
                // Gọi hàm xử lý dữ liệu nếu cần
                handleSelectedJobType(jobType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý khi không có mục nào được chọn
            }
        }
    }

    // Hàm để xử lý dữ liệu khi một loại công việc được chọn từ Spinner
    private fun handleSelectedJobType(jobType: String) {
        // Xử lý dữ liệu ở đây, ví dụ:
        Log.d("Selected Job Type", jobType)
    }


}