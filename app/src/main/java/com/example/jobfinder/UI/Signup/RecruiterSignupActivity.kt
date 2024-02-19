package com.example.jobfinder.UI.Signup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.jobfinder.R
import com.example.jobfinder.Utils.Calendar
import com.example.jobfinder.Utils.CalendarToggleState
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityRecruiterSignupBinding

class RecruiterSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecruiterSignupBinding
    private var isPassVisible = PasswordToggleState(false)
    private var isCalendarVisible = CalendarToggleState(false)
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mở icon, show password và ngược lại
        VerifyField.changeIconShowPassword(binding.passwordInputLayout, isPassVisible, binding.password)

        // chọn ngày thành lập
        fragmentManager = supportFragmentManager
        binding.foundationDay.setOnClickListener{
            val title = getString(R.string.rec_foundationDay)
            val showTextPlace = binding.foundationDay
            Calendar.showDatePickerDialog(getActivityFragmentManager(), isCalendarVisible, title, showTextPlace)
        }

        // từ recruiter trở về login
        binding.returnbackLogin.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // Phương thức để trả về FragmentManager của Activity
    private fun getActivityFragmentManager(): FragmentManager {
        return supportFragmentManager
    }
}