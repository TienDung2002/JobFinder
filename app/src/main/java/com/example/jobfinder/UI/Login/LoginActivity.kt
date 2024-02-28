package com.example.jobfinder.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jobfinder.R
import com.example.jobfinder.UI.ForgotPassword.ForgotPassActivity
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.UI.Register.RecruiterRegisterActivity
import com.example.jobfinder.UI.Register.SeekerRegisterActivity
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var isPassVisible = PasswordToggleState(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // gọi hàm đổi icon và ẩn hiện password
        VerifyField.changeIconShowPassword(binding.passwordTextInputLayout, isPassVisible, binding.userPassLogin)


        // Lấy role từ bên splashscreen
        val userType = intent.getIntExtra("user_type", -1)


        // Mở register
        binding.openRegisterActi.setOnClickListener{
            if (PreventDoubleClick.checkClick()) {
                if (userType == 0) {
                    val intent = Intent(this, SeekerRegisterActivity::class.java)
                    startActivity(intent)
                } else if (userType == 1) {
                    val intent = Intent(this, RecruiterRegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }


        // Hiển thị tiêu đề dựa vào role đã chọn
        if (userType == 0) {
            binding.titleLogin1.setText(R.string.welcome_seek1)
            binding.titleLogin2.setText(R.string.welcome_seek2)
        } else {
            binding.titleLogin1.setText(R.string.welcome_rec1)
            binding.titleLogin2.setText(R.string.welcome_rec2)
        }


        // Xác nhận để Login
        binding.btnLogin.setOnClickListener {
            if (PreventDoubleClick.checkClick()) {
                val emailInput = binding.userEmailLogin.text.toString()
                val passInput = binding.userPassLogin.text.toString()
                val isEmailValid = emailInput.isNotEmpty() && VerifyField.isValidEmail(emailInput)
                val isPassValid = passInput.isNotEmpty()

                binding.userEmailLogin.error = if (isEmailValid) null else getString(R.string.error_invalid_email)
                binding.userPassLogin.error = if (isPassValid) null else getString(R.string.error_pass)

                if (isEmailValid && isPassValid) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    checkToAutoFocus(isEmailValid, isPassValid)
                }
            }
        }


        // Quên mật khẩu
        binding.moveToForgotBtn.setOnClickListener{
            if (PreventDoubleClick.checkClick()) {
                val intent = Intent(this, ForgotPassActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<TextInputEditText>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.userEmailLogin)
                    1 -> invalidFields.add(binding.userPassLogin)
                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }
}