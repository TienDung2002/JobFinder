package com.example.jobfinder.UI.Wallet

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityWalletBinding
import com.example.jobfinder.databinding.ActivityWalletHistoryBinding

class WalletHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWalletHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}