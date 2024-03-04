package com.example.jobfinder.UI.Home

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//        binding.webView.loadUrl("https://www.youtube.com/")
        val handler = Handler()
        handler.postDelayed({
            binding.webView.loadUrl("https://www.youtube.com/")
        }, 2000) // Đợi 2000ms (2 giây) trước khi load URL
    }
}