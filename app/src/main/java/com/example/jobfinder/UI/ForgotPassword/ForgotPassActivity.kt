package com.example.jobfinder.UI.ForgotPassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityForgotPassBinding

class ForgotPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // thêm fragment vào activity
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment = ForgotPassFragment()
        transaction.add(R.id.fragment_FrameLayout, fragment)
//        transaction.replace(R.id.fragment_FrameLayout, fragment)
        transaction.commit()


        // nút quay lại Login
        binding.returnLoginBtn.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


        // nút reset password
//        binding.btnReset.setOnClickListener {
//
//        }

    }
}