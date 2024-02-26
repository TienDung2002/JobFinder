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

        // Mở signup
        binding.openSignupActi.setOnClickListener{
            val userType = intent.getIntExtra("user_type", 0)
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
            val emailField = binding.userEmailLogin
            val emailInput = binding.userEmailLogin.text.toString()
            val passField = binding.userPassLogin
            val passInput = binding.userPassLogin.text.toString()

            if (emailInput.isEmpty() || !VerifyField.isValidEmail(emailInput)) {
                emailField.error = getString(R.string.error_invalid_email)
            } else {
                emailField.error = null
            }

            if (passInput.isEmpty()) {
                passField.error = getString(R.string.error_pass)
            } else {
                passField.error = null
            }

            if ((emailInput.isNotEmpty() && VerifyField.isValidEmail(emailInput)) && passInput.isNotEmpty()) {
                // Chuyển đến màn home
//                if (PreventDoubleClick.checkClick()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
//                }
            } else {
                Toast.makeText(this, getString(R.string.error_email_pass), Toast.LENGTH_SHORT).show()
            }
        }
    }

}