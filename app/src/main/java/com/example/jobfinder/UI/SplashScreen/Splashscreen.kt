package com.example.jobfinder.UI.SplashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class Splashscreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.iconSplash.alpha = 0f
        binding.iconSplash.animate().setDuration(2000).alpha(1f).withEndAction{
//            val intent = Intent(this, SelectRoleActivity::class.java)
//            startActivity(intent)

            // Kiểm tra trạng thái đăng nhập
            if (auth.currentUser != null) {
                // Người dùng đã đăng nhập, chuyển thẳng tới HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                // Người dùng chưa đăng nhập, chuyển tới SelectRoleActivity
                startActivity(Intent(this, SelectRoleActivity::class.java))
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }.start()
    }
}