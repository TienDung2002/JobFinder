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
import com.example.jobfinder.UI.Notifications.NotificationsFragment
import com.example.jobfinder.UI.SplashScreen.SelectRoleActivity
import com.example.jobfinder.UI.UsersProfile.UserDetailActivity
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private var backPressedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //firebase
        auth = FirebaseAuth.getInstance()

        // ngay đầu add frag vào home luôn
        FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentNuser())


        // trả về result về login để đóng activity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)


        // thanh Navigation
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            handleNavigation(menuItem.itemId)
        }

    }

    override fun onResume() {
        super.onResume()
        backPressedCount = 0 // Reset lại backPressedCount khi activity resume
        updateNavigationBar()
    }

    // Bấm 1 lần để hỏi, lần thứ 2 sẽ thoát ứng dụng
    override fun onBackPressed() {
        if (backPressedCount >= 1) {
            setResult(Activity.RESULT_CANCELED)
            super.onBackPressed() // đóng activity
            finish()
        } else {
            backPressedCount++
            Toast.makeText(this, getString(R.string.backpress_ask), Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                backPressedCount = 0
            }, 2000) // Reset backPressedCount sau 2 giây
        }
    }

    // Xử lí các bottom Navigation
    private fun handleNavigation(itemId: Int): Boolean {
        if (isCurrentFragment(itemId)) {
            return true // Không cần reload lại fragment nếu đã ở trong fragment mục tiêu
        }
        when(itemId) {
            R.id.home -> {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentNuser())
//                FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentBuser())
                return true
            }
            R.id.managermentJob  -> {
                startActivity(Intent(this, JobsActivity::class.java))
                return true
            }
            R.id.notify -> {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, NotificationsFragment())
                return true
            }
            R.id.profile -> {
                startActivity(Intent(this, UserDetailActivity::class.java))
                return true
            }
            R.id.logout -> {
                auth.signOut()
                finish()
                startActivity(Intent(this, SelectRoleActivity::class.java))
                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                return false
            }
        }
    }

    // Kiểm tra fragment hiện tại và cập nhật icon trên thanh điều hướng dưới cùng (navbar) tương ứng
    private fun updateNavigationBar() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.HomeFrameLayout)
        when (currentFragment) {
            is HomeFragmentNuser -> binding.bottomNavView.selectedItemId = R.id.home
            is NotificationsFragment -> binding.bottomNavView.selectedItemId = R.id.notify
        }
    }

    // tránh reload lại khi đang hiển thị fragment mà nhấn nav lần nữa trùng fragment đó
    private fun isCurrentFragment(itemId: Int): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.HomeFrameLayout)
        return when (itemId) {
            R.id.home -> currentFragment is HomeFragmentNuser
            R.id.notify -> currentFragment is NotificationsFragment
            else -> false
        }
    }

}