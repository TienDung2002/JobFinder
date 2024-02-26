package com.example.jobfinder.UI.Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.jobfinder.R
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.Utils.Calendar
import com.example.jobfinder.Utils.CalendarToggleState
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityRecruiterRegisterBinding

class RecruiterRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecruiterRegisterBinding
//    private var isPassVisible = PasswordToggleState(false)
//    private var isCalendarVisible = CalendarToggleState(false)
//    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mở icon, show password và ngược lại
//        VerifyField.changeIconShowPassword(binding.passwordInputLayout, isPassVisible, binding.password)

//        // chọn ngày thành lập
//        fragmentManager = supportFragmentManager
//        binding.foundationDay.setOnClickListener{
//            val title = getString(R.string.rec_foundationDay)
//            val showTextPlace = binding.foundationDay
//            Calendar.showDatePickerDialog(getActivityFragmentManager(), isCalendarVisible, title, showTextPlace)
//        }

        // Đăng ký
        binding.btnRegister.setOnClickListener{
            val nameInput = binding.recName.text.toString()
            val hotlineInput = binding.recHotline.text.toString()
            val addressInput = binding.recAddress.text.toString()
            val emailInput = binding.recEmail.text.toString()
            val passInput = binding.password.text.toString()
            val repassInput = binding.reEnterPass.text.toString()

            val isValidName = nameInput.isNotEmpty()
            val isValidHotline = VerifyField.isValidPhoneNumber(hotlineInput)
            val isValidAddress = addressInput.isNotEmpty()
            val isValidEmail = VerifyField.isValidEmail(emailInput)
            val isValidPassword = passInput.isNotEmpty()
            val isValidRePassword = repassInput.isNotEmpty() && repassInput == passInput

            binding.recName.error = if (isValidName) null else getString(R.string.error_invalid_name)
            binding.recHotline.error = if (isValidHotline) null else getString(R.string.error_invalid_hotline)
            binding.recAddress.error = if (isValidAddress) null else getString(R.string.error_invalid_addr)
            binding.recEmail.error = if (isValidEmail) null else getString(R.string.error_invalid_email)
            binding.password.error = if (isValidPassword) null else getString(R.string.error_pass)
            binding.reEnterPass.error = if (isValidRePassword) null else getString(R.string.error_invalid_reEnterPass)

            if (isValidName && isValidHotline && isValidAddress && isValidEmail && isValidPassword && isValidRePassword) {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.error_email_pass), Toast.LENGTH_SHORT).show()
            }
        }

        // từ recruiter trở về login
        binding.returnbackLogin.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // Phương thức để trả về FragmentManager của Activity
//    private fun getActivityFragmentManager(): FragmentManager {
//        return supportFragmentManager
//    }
}