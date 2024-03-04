package com.example.jobfinder.UI.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.UI.Login.LoginActivity
import com.example.jobfinder.databinding.ActivitySelectRoleBinding

class SelectRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRoleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // mở login role tuyển dụng
        binding.loginasRecruiter.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", "BUser")
            startActivity(intent)
        }

        // mở login role người tìm việc
        binding.loginasSeeker.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", "NUser")
            startActivity(intent)
        }
    }

    // hàm mở fragment
//    private fun openFragment(fragment: Fragment){
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainer, fragment)
//        transaction.addToBackStack(null)    // ấn nút back trên thiết bị cho phép đóng fragments
//        transaction.commit()
//    }
}