package com.example.jobfinder.UI.Home
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.jobfinder.R
import com.example.jobfinder.UI.Jobs.JobsActivity
import com.example.jobfinder.UI.Notifications.NotificationsActivity
import com.example.jobfinder.UI.UsersProfile.UserDetailActivity
import com.example.jobfinder.UI.Wallet.WalletActivity
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var backPressedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ngay đầu add frag vào home luôn
        FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, TestFragment())


        // trả về result về login để đóng activity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)


        // Menu
        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, TestFragment())
                R.id.job  -> { startActivity(Intent(this, JobsActivity::class.java)) }
                R.id.notify -> { startActivity(Intent(this, NotificationsActivity::class.java)) }
                R.id.wallet -> { startActivity(Intent(this, WalletActivity::class.java)) }
                R.id.profile -> { startActivity(Intent(this, UserDetailActivity::class.java)) }

                else -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }



    }






    override fun onResume() {
        super.onResume()
        backPressedCount = 0 // Reset lại backPressedCount khi activity resume
    }

    // Bấm 1 lần để hỏi, lần thứ 2 sẽ thoát ứng dụng
    override fun onBackPressed() {
        if (backPressedCount >= 1) {
            super.onBackPressed() // đóng activity
        } else {
            backPressedCount++
            Toast.makeText(this, getString(R.string.backpress_ask), Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                backPressedCount = 0
            }, 2000) // Reset backPressedCount sau 2 giây
        }
    }

}