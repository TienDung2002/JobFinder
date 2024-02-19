package com.example.jobfinder.UI.Signup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.R
import com.example.jobfinder.UI.Login.LoginActivity
import com.example.jobfinder.Util.PasswordToggleState
import com.example.jobfinder.Util.VerifyField
import com.example.jobfinder.databinding.ActivityRecruiterSignupBinding

class RecruiterSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecruiterSignupBinding
    private var isPassVisible = PasswordToggleState(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        VerifyField.changeIconShowPassword(binding.passwordInputLayout, isPassVisible, binding.password)

        // trở về login
        binding.returnbackLogin.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}