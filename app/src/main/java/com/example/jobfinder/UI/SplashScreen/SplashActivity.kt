package com.example.jobfinder.UI.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.UI.Login.LoginActivity
import com.example.jobfinder.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mở login role tuyển dụng
        binding.loginasRecruiter.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", 1)
            startActivity(intent)
        }

        // mở login role người tìm việc
        binding.loginasSeeker.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("user_type", 0)
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