package com.example.jobfinder.UI.Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jobfinder.R
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivitySeekerRegisterBinding

class SeekerRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivitySeekerRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đăng ký
        binding.btnRegister.setOnClickListener{
            val nameInput = binding.seekName.text.toString()
            val phoneInput = binding.seekPhonenums.text.toString()
            val addressInput = binding.seekAddress.text.toString()
            val emailInput = binding.seekEmail.text.toString()
            val passInput = binding.password.text.toString()
            val repassInput = binding.reEnterPass.text.toString()

            val isValidName = nameInput.isNotEmpty()
            val isValidPhone = VerifyField.isValidPhoneNumber(phoneInput)
            val isValidAddress = addressInput.isNotEmpty()
            val isValidEmail = VerifyField.isValidEmail(emailInput)
            val isValidPassword = passInput.isNotEmpty()
            val isValidRePassword = repassInput.isNotEmpty() && repassInput == passInput

            binding.seekName.error = if (isValidName) null else getString(R.string.error_invalid_name)
            binding.seekPhonenums.error = if (isValidPhone) null else getString(R.string.error_invalid_phone)
            binding.seekAddress.error = if (isValidAddress) null else getString(R.string.error_invalid_addr)
            binding.seekEmail.error = if (isValidEmail) null else getString(R.string.error_invalid_email)
            binding.password.error = if (isValidPassword) null else getString(R.string.error_pass)
            binding.reEnterPass.error = if (isValidRePassword) null else getString(R.string.error_invalid_reEnterPass)

            if (isValidName && isValidPhone && isValidAddress && isValidEmail && isValidPassword && isValidRePassword) {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.error_email_pass), Toast.LENGTH_SHORT).show()
            }
        }

        // trở về login
        binding.returnbackLogin.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
}