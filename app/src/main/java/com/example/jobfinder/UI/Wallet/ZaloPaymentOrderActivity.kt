package com.example.jobfinder.UI.Wallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityZaloPaymentOrderBinding

class ZaloPaymentOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityZaloPaymentOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZaloPaymentOrderBinding.inflate(layoutInflater)
        setContentView(binding.root )

        val amount = intent.getDoubleExtra("amount", 0.0)

        binding.inputNum.text = amount.toString()

        binding.confirmOrder.setOnClickListener {

        }
    }
}