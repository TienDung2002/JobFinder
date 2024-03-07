package com.example.jobfinder.UI.SplashScreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.UI.Login.LoginActivity
import com.example.jobfinder.databinding.ActivitySelectRoleBinding

class SelectRoleActivity : AppCompatActivity() {
    private val LOGIN_REQUEST_CODE = 100 // Đặt một mã request
    private lateinit var binding: ActivitySelectRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRoleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // mở login role tuyển dụng
        binding.loginasRecruiter.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", "BUser")
//            startActivity(intent)
            startActivityForResult(intent, LOGIN_REQUEST_CODE) // Mở LoginActivity với mã request

        }

        // mở login role người tìm việc
        binding.loginasSeeker.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", "NUser")
//            startActivity(intent)
            startActivityForResult(intent, LOGIN_REQUEST_CODE) // Mở LoginActivity với mã request
        }


    }


    // Xử lý kết quả từ LoginActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Đăng nhập thành công, kết thúc cả SelectRoleActivity và LoginActivity
            finish()
        }
    }



}
