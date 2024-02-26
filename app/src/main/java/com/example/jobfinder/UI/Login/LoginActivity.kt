package com.example.jobfinder.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jobfinder.R
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.UI.Register.RecruiterRegisterActivity
import com.example.jobfinder.UI.Register.SeekerRegisterActivity
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var isPassVisible = PasswordToggleState(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gọi hàm đổi icon và ẩn hiện password
        VerifyField.changeIconShowPassword(binding.passwordTextInputLayout, isPassVisible, binding.userPassLogin)

        // Mở register
        binding.openRegisterActi.setOnClickListener{
            val userType = intent.getIntExtra("user_type", -1)

            if (userType == 0) {
                val intent = Intent(this, SeekerRegisterActivity::class.java)
                startActivity(intent)
            } else if (userType == 1) {
                val intent = Intent(this, RecruiterRegisterActivity::class.java)
                startActivity(intent)
            }
        }

        // Login
        binding.btnLogin.setOnClickListener {
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
                Toast.makeText(this, getString(R.string.error_email_pass), Toast.LENGTH_SHORT).show()
            }
        }

    }

}