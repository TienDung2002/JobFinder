package com.example.jobfinder.UI.Admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityAdminHomeBinding
import com.google.android.material.textfield.TextInputEditText

class AdminHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminHomeBinding
    private var backPressedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // trả về result về login admin để đóng activity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)





    }


    override fun onResume() {
        super.onResume()
        backPressedCount = 0 // Reset lại backPressedCount khi activity resume
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
}