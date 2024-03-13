package com.example.jobfinder.UI.Wallet

import WalletFragment
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.jobfinder.R
import com.example.jobfinder.Utils.FragmentHelper.replaceFragment
import com.example.jobfinder.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
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

        binding.addWalletFtBtn.setOnClickListener {
            // Thay thế WalletFragment bằng AddWalletFragment
            replaceFragment(AddWalletFragment())
            binding.walletTitle.setText(R.string.add_wallet)
            backCheck = true
        }
        binding.addWalletFtTxt.setOnClickListener {
            // Thay thế WalletFragment bằng AddWalletFragment
            replaceFragment(AddWalletFragment())
            binding.walletTitle.setText(R.string.add_wallet)
            backCheck = true
        }
        replaceFragment(WalletFragment())

        // back bằng nút trên màn hình
        binding.backButton.setOnClickListener{
            if(backCheck){
                replaceFragment(WalletFragment())
                binding.walletTitle.setText(R.string.wallet_title)
                backCheck= false
            }else {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }


    // back bằng nút hoặc vuốt trên thiết bị
    override fun onBackPressed() {
        super.onBackPressed()
        // Khởi tạo Intent để quay lại HomeActivity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
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
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.wallet_activity_framelayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun onWalletAddedSuccessfully() {
        binding.walletTitle.setText(R.string.wallet_title)
    }

}