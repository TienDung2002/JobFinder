package com.example.jobfinder.UI.Home
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.UI.JobsManagement.JobsManagementActivity
import com.example.jobfinder.UI.Notifications.NotificationsFragment
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.UI.SplashScreen.SelectRoleActivity
import com.example.jobfinder.UI.UsersProfile.UserDetailActivity
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private var backPressedCount = 0
    private var addingFragmentInProgress = false
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()

        // Khởi tạo viewmodel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        // Chỉ add fragment vào home khi trạng thái hiện tại là null (tránh xoay màn hình lại add lại gây lỗi)
        if (savedInstanceState == null) {
            addingFragmentInProgress = true
            // gọi hàm getUserRole từ trong Utils.GetData
            GetData.getUserRole { role ->
                role?.let {
                    // Gán giá trị chỉ khi role không null (logic là thế nhưng hàm getUserRole t cho trả về "null string" thay vì null)
                    viewModel.userRole = it
                    addFragmentDefault(viewModel.userRole)
                    updateNavigationBar()
                    addingFragmentInProgress = false
                }
            }
        } else {
            // Khôi phục trạng thái của activity từ bundle
            viewModel.userRole = savedInstanceState.getString("userRole", "")
            binding.animationView.visibility = View.GONE
        }


        // trả về result về login để đóng activity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)


        // thanh Navigation
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            if (!addingFragmentInProgress) { // Kiểm tra xem quá trình thêm fragment có đang diễn ra không
                handleNavigation(menuItem.itemId)
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        backPressedCount = 0 // Reset lại backPressedCount khi activity resume
        updateNavigationBar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Lưu trạng thái của activity vào bundle
        outState.putString("userRole", viewModel.userRole)
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
                if (viewModel.userRole == "BUser") {
                    FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentBuser())
                } else {
                    FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentNuser())
                }
                return true
            }
            R.id.managermentJob  -> {
                if (viewModel.userRole == "BUser") {
                    startActivity(Intent(this, JobsManagementActivity::class.java))
                } else {
                    startActivity(Intent(this, JobsManagementActivity::class.java))
                }
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
//            R.id.logout -> {
//                auth.signOut()
//                finish()
//                startActivity(Intent(this, SelectRoleActivity::class.java))
//                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
//                return true
//            }
            else -> {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                return false
            }
        }
    }


    // Hàm này thực hiện việc thêm fragment default dựa trên giá trị của userRole
    private fun addFragmentDefault(curRole: String) {
        when (curRole) {
            "BUser" -> {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentBuser())
                binding.animationView.visibility = View.GONE
            }
            "NUser" -> {
                FragmentHelper.replaceFragment(supportFragmentManager, binding.HomeFrameLayout, HomeFragmentNuser())
                binding.animationView.visibility = View.GONE
            }
            else -> {
                Toast.makeText(this, "Có lỗi xảy ra khi thêm fragment default", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Kiểm tra fragment hiện tại và cập nhật icon trên thanh điều hướng dưới cùng (navbar) tương ứng
    private fun updateNavigationBar() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.HomeFrameLayout)
        when {
            currentFragment is HomeFragmentNuser && viewModel.userRole == "NUser" -> {
                binding.bottomNavView.selectedItemId = R.id.home
                binding.animationView.visibility = View.GONE
            }
            currentFragment is HomeFragmentBuser && viewModel.userRole == "BUser" -> {
                binding.bottomNavView.selectedItemId = R.id.home
                binding.animationView.visibility = View.GONE
            }
            currentFragment is NotificationsFragment -> {
                binding.bottomNavView.selectedItemId = R.id.notify
                binding.animationView.visibility = View.GONE
            }
        }
    }

    // tránh reload lại khi đang hiển thị fragment mà nhấn nav lần nữa trùng fragment đó
    private fun isCurrentFragment(itemId: Int): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.HomeFrameLayout)
        return when (itemId) {
            R.id.home -> {
                if (viewModel.userRole == "BUser") {
                    currentFragment is HomeFragmentBuser
                } else {
                    currentFragment is HomeFragmentNuser
                }
            }
            R.id.notify -> currentFragment is NotificationsFragment
            else -> false
        }
    }
    

}