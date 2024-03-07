package com.example.jobfinder.UI.Home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var backPressedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // trả về result về login để đóng activity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)



        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.animationView.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.animationView.visibility = View.GONE
            }
        }
        binding.webView.loadUrl("https://www.youtube.com/")
//        val handler = Handler()
//        handler.postDelayed({
//            binding.webView.loadUrl("https://www.youtube.com/")
//        }, 2000) // Đợi 2000ms (2 giây) trước khi load URL
    }

    
    override fun onResume() {
        super.onResume()
        backPressedCount = 0 // Reset lại backPressedCount khi activity resume
    }

    // Bấm 2 lần để hỏi, lần thứ 3 sẽ thoát ứng dụng
    override fun onBackPressed() {
        if (backPressedCount == 2) {
            super.onBackPressed() // đóng activity
        } else {
            if (backPressedCount == 1) {
                Toast.makeText(this, getString(R.string.backpress_ask), Toast.LENGTH_SHORT).show()
            }
            backPressedCount++
            Handler(Looper.getMainLooper()).postDelayed({
                backPressedCount = 0
            }, 2000) // Reset backPressedCount sau 2 giây
        }
    }

}