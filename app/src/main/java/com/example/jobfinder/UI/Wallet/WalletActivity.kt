package com.example.jobfinder.UI.Wallet

import WalletFragment
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.jobfinder.R
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityWalletBinding
import com.google.firebase.auth.FirebaseAuth

class WalletActivity : AppCompatActivity() , WalletFragment.DataLoadListener {
    private lateinit var binding: ActivityWalletBinding
    private var isExpanded = true
    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation
    private var backCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fadeInAnimation= AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeInAnimation.duration = 500
        fadeOutAnimation= AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        fadeOutAnimation.duration = 500

        binding.mainFtBtn.setOnClickListener {
            // Đảo ngược trạng thái mở rộng và cập nhật giao diện
            isExpanded = !isExpanded
            updateFABVisibility()
        }

        // add fragment mặc định khi mới mở
        FragmentHelper.replaceFragment(supportFragmentManager, binding.walletActivityFramelayout, WalletFragment())

        binding.addWalletFtBtn.setOnClickListener {
            // Thay thế WalletFragment bằng AddWalletFragment
            FragmentHelper.replaceFragment(supportFragmentManager, binding.walletActivityFramelayout, AddWalletFragment())
            binding.walletTitle.setText(R.string.add_wallet)
            backCheck = true
        }


        binding.addWalletFtTxt.setOnClickListener {
            // Thay thế WalletFragment bằng AddWalletFragment
            FragmentHelper.replaceFragment(supportFragmentManager, binding.walletActivityFramelayout, AddWalletFragment())
            binding.walletTitle.setText(R.string.add_wallet)
            backCheck = true
        }

        binding.walletHistoryFtBtn.setOnClickListener{
            startActivity(Intent(this, WalletHistoryActivity::class.java))
            finish()
        }

        binding.walletHistoryFtTxt.setOnClickListener{
            startActivity(Intent(this, WalletHistoryActivity::class.java))
            finish()
        }

        // back bằng nút trên màn hình
        binding.backButton.setOnClickListener{
            if(backCheck){
                FragmentHelper.replaceFragment(supportFragmentManager, binding.walletActivityFramelayout, WalletFragment())
                binding.walletTitle.setText(R.string.wallet_title)
                backCheck= false
            }else {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    // Xử lý khi dữ liệu đã tải xong bên WalletFragment (nếu adapter không rỗng)
    override fun onDataLoaded() {
        binding.animationView.visibility = View.GONE
    }

    // Kiểm tra nếu adapter rỗng, ẩn animationView, hiện noWalletCard
    override fun onDataLoadedEmpty(isListEmpty: Boolean) {
        val walletFragment = supportFragmentManager.findFragmentById(R.id.wallet_activity_framelayout) as? WalletFragment
        if (isListEmpty) {
            walletFragment?.updateNoWalletCardVisibility(View.VISIBLE)
        } else {
            walletFragment?.updateNoWalletCardVisibility(View.GONE)
        }
        binding.animationView.visibility = View.GONE
    }

    private fun updateFABVisibility() {
        if (isExpanded) {
            binding.walletHistoryFtTxt.startAnimation(fadeOutAnimation)
            binding.walletHistoryFtBtn.startAnimation(fadeOutAnimation)
            binding.addWalletFtTxt.startAnimation(fadeOutAnimation)
            binding.addWalletFtBtn.startAnimation(fadeOutAnimation)
            binding.walletHistoryFtTxt.visibility = View.GONE
            binding.walletHistoryFtBtn.visibility = View.GONE
            binding.addWalletFtTxt.visibility = View.GONE
            binding.addWalletFtBtn.visibility = View.GONE
            binding.walletHistoryFtBtn.isClickable = false
            binding.addWalletFtBtn.isClickable = false
            binding.walletHistoryFtTxt.isClickable = false
            binding.addWalletFtTxt.isClickable = false
        } else {
            binding.walletHistoryFtTxt.startAnimation(fadeInAnimation)
            binding.walletHistoryFtBtn.startAnimation(fadeInAnimation)
            binding.addWalletFtTxt.startAnimation(fadeInAnimation)
            binding.addWalletFtBtn.startAnimation(fadeInAnimation)
            binding.walletHistoryFtTxt.visibility = View.VISIBLE
            binding.walletHistoryFtBtn.visibility = View.VISIBLE
            binding.addWalletFtTxt.visibility = View.VISIBLE
            binding.addWalletFtBtn.visibility = View.VISIBLE
            binding.walletHistoryFtBtn.isClickable = true
            binding.addWalletFtBtn.isClickable = true
            binding.walletHistoryFtTxt.isClickable = true
            binding.addWalletFtTxt.isClickable = true
        }
    }
}