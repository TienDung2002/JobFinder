package com.example.jobfinder.UI.UserDetailInfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.databinding.ActivityWatchNuserReviewedBinding

class WNuserReviewedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchNuserReviewedBinding
    private val viewModel: WNuserReviewedViewModel by viewModels()
    private lateinit var adapter: WNuserReviewedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchNuserReviewedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nút back về
        binding.Backbtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // bấm ảnh mở pro5 ứng viên
        binding.wnuserAvatar.setOnClickListener {

        }

        binding.noDataImage.visibility = View.GONE

        // Fetch dữ liệu
        viewModel.fetchReviews()
        binding.noDataImage.visibility = View.GONE


        // Quan sát data adapter từ ViewModel
        viewModel.reviewsList.observe(this, Observer { reviews ->
            adapter.updateData(reviews)
            binding.noDataImage.visibility = View.GONE
        })


        // gán data vào adapter
        adapter = WNuserReviewedAdapter(listOf(), binding.noDataImage)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter






    }
}