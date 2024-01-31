package com.example.jobfinder.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.jobfinder.General.PreventDoubleClick
import com.example.jobfinder.R
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var isPassVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeIconShowPassword()

        // Login
        binding.btnLogin.setOnClickListener {
            val emailField = binding.userEmailLogin
            val emailInput = binding.userEmailLogin.text.toString()
            val passField = binding.userPassLogin
            val passInput = binding.userPassLogin.text.toString()

            if (emailInput.isEmpty() || !isValidEmail(emailInput)) {
                emailField.error = getString(R.string.error_invalid_email)
            } else {
                emailField.error = null
            }

            if (passInput.isEmpty()) {
                passField.error = getString(R.string.error_pass)
            } else {
                passField.error = null
            }

            if ((emailInput.isNotEmpty() && isValidEmail(emailInput)) && passInput.isNotEmpty()) {
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

    // thay đổi icon mỗi lần click ẩn hiện password
    private fun changeIconShowPassword(){
        binding.passwordTextInputLayout.setEndIconOnClickListener{
            isPassVisible = !isPassVisible
            togglePasswordVisible(binding.userPassLogin)
        }
    }

    // logic ẩn hiện password
    private fun togglePasswordVisible(editText: TextInputEditText) {
        if (isPassVisible) editText.transformationMethod = null // transformationMethod = null thì xem dc text
        else editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.text?.let { editText.setSelection(it.length) } //di chuyển con trỏ về cuối text
    }

    // kiểm tra input đúng định dạng email
    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}