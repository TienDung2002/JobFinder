package com.example.jobfinder.UI.JobPosts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class JobpostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobpostsBinding
    private var shift = "none"
    private lateinit var auth: FirebaseAuth
    private var shiftChoose = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobpostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()

        binding.postJobTitle.isClickable = true
        binding.recShift1.isClickable = true
        binding.recShift2.isClickable = true
        binding.postJobStartTime.isClickable = true
        binding.postJobEndTime.isClickable = true
        binding.postJobEmpAmount.isClickable = true
        binding.postJobSalary.isClickable = true
        binding.postJobAddress.isClickable = true
        binding.postJobDes.isClickable = true
        binding.postJobBtn.isClickable = true

        //back btn
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        //check box
        binding.recShift1.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                binding.recShift2.isChecked = false
                shift = "1"
                shiftChoose = true
            }
        }

        binding.recShift2.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                binding.recShift1.isChecked = false
                shift = "2"
                shiftChoose = true
            }
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
            val workShift = shift
            val startTime = binding.postJobStartTime.text.toString()
            val endTime = binding.postJobEndTime.text.toString()
            val empAmount = binding.postJobEmpAmount.text.toString()
            val salary = binding.postJobSalary.text.toString()
            val address = binding.postJobAddress.text.toString()
            val jobDes= binding.postJobDes.text.toString()

            //field check
            if(!shiftChoose){
                Toast.makeText(binding.root.context, getString(R.string.no_shift_choose), Toast.LENGTH_SHORT).show()
            }
            val isValidStartTime = VerifyField.isEmpty(startTime.trim())
            val isValidEndTime = VerifyField.isEmpty(endTime.trim())
            val isValidTitle = VerifyField.isEmpty(title.trim())
            val isValidEmpAmount =  VerifyField.maxEmpAmount(empAmount)
            val isValidSalary =  VerifyField.isEmpty(salary.trim())
            val isValidAddress = VerifyField.isEmpty(address.trim())
            val isValidJobDes = VerifyField.isEmpty(jobDes.trim())
            val isValidWorkDate = GetData.compareDates(startTime, endTime)



            if(!isValidWorkDate){
                Toast.makeText(binding.root.context, getString(R.string.end_time_be4_start),Toast.LENGTH_SHORT).show()
            }
            binding.postJobTitle.error = if (isValidTitle) null else getString(R.string.no_post_job_title)
            binding.postJobEmpAmount.error = if(isValidEmpAmount) null else getString(R.string.no_emp_amount)
            binding.postJobSalary.error = if(isValidSalary) null else getString(R.string.no_salary)
            binding.postJobAddress.error = if(isValidAddress) null else getString(R.string.no_address)
            binding.postJobDes.error = if(isValidJobDes) null else getString(R.string.no_job_des)
            binding.postJobStartTime.error= if(isValidStartTime) null else getString(R.string.no_start_time)
            binding.postJobEndTime.error= if(isValidEndTime) null else getString(R.string.no_end_time)

            if( shiftChoose && isValidTitle && isValidAddress && isValidEmpAmount && isValidSalary && isValidJobDes && isValidStartTime && isValidEndTime && isValidWorkDate){

                val uid = auth.currentUser?.uid

                val jobId= FirebaseDatabase.getInstance().getReference("Job").child(uid.toString()).push().key
                val totalWorkDay = GetData.countDaysBetweenDates(startTime, endTime)
                val totalWorkHour= totalWorkDay * 8
                val OneEmpSalary = GetData.multiplyStrings(totalWorkHour.toString(), salary)
                val totalSalary = GetData.multiplyStrings(empAmount, OneEmpSalary)
                val date = GetData.getCurrentDateTime()

                FirebaseDatabase.getInstance().getReference("UserBasicInfo").child(uid.toString()).get().addOnSuccessListener { data ->
                    if(data.exists()) {
                        val BUserName =
                            data.child("name").getValue(String::class.java).toString()
                        val walletAmountRef = FirebaseDatabase.getInstance().getReference("WalletAmount").child(uid.toString())
                        walletAmountRef.get().addOnSuccessListener { walletData ->
                            if(walletData.exists()) {
                                val walletAmount =
                                    walletData.child("amount").getValue(String::class.java)
                                        .toString()

                                if(GetData.compareFloatStrings(walletAmount, totalSalary)) {

                                    binding.postJobTitle.isClickable = false
                                    binding.recShift1.isClickable = false
                                    binding.recShift2.isClickable = false
                                    binding.postJobStartTime.isClickable = false
                                    binding.postJobEndTime.isClickable = false
                                    binding.postJobEmpAmount.isClickable = false
                                    binding.postJobSalary.isClickable = false
                                    binding.postJobAddress.isClickable = false
                                    binding.postJobDes.isClickable = false
                                    binding.postJobBtn.isClickable = false

                                    val newJob = JobModel(
                                        jobId,
                                        title,
                                        workShift,
                                        startTime,
                                        endTime,
                                        empAmount,
                                        salary,
                                        address,
                                        jobDes,
                                        totalSalary,
                                        date,
                                        "0",
                                        BUserName
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
                                                val notificationsRowModel = NotificationsRowModel(
                                                    notiId,
                                                    "Admin",
                                                    "${getString(R.string.post_job_success)}.\n" +
                                                            "${getString(R.string.post_job_title)}: $title",
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

}