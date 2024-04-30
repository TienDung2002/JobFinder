package com.example.jobfinder.UI.FindNewJobs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentCustomFilterMenuBinding
import com.example.jobfinder.databinding.FragmentForgotPassBinding
import com.google.android.material.slider.RangeSlider


class Custom_filter_menu_Fragment : Fragment() {
    private lateinit var binding: FragmentCustomFilterMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(  // khởi tạo UI fragment
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomFilterMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //tương tác với UI sau khi onCreateView
        super.onViewCreated(view, savedInstanceState)

        // Đặt các button về mặc định
        selectedFilterDefault()

        // Job title radio
        binding.JTItems.setOnCheckedChangeListener { group, checkedId ->
            val selectedButton = when (checkedId) {
                binding.JTAll.id -> binding.JTAll
                binding.JTAtoZ.id -> binding.JTAtoZ
                else -> null
            }
            selectedButton?.setBackgroundResource(R.drawable.custom_filter_btn_selected)
//            selectedButton?.setBackgroundResource(R.drawable.state_filter_btn)

            // Đặt background cho các button còn lại thành mặc định
            for (i in 0 until group.childCount) {
                val button = group.getChildAt(i) as Button
                if (button.id != checkedId) {
                    button.setBackgroundResource(R.drawable.custom_filter_btn_default)
                }
            }
        }


        // recName radio
        binding.recNameItems.setOnCheckedChangeListener { group, checkedId ->
            // Xử lý sự kiện khi lựa chọn thay đổi trong recNameItemsGroup
            // Bạn có thể thay đổi giao diện của các Button dựa trên checkedId
        }


        // postedTime radio
        binding.postedTimeItems.setOnCheckedChangeListener { group, checkedId ->
            // Xử lý sự kiện khi lựa chọn thay đổi trong PTItemsGroup
            // Bạn có thể thay đổi giao diện của các Button dựa trên checkedId
        }


        // work time radio
        binding.WSItems.setOnCheckedChangeListener { group, checkedId ->
            // Xử lý sự kiện khi lựa chọn thay đổi trong PTItemsGroup
            // Bạn có thể thay đổi giao diện của các Button dựa trên checkedId
        }


        // seekbar slider
        binding.rangeslider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
            }
        })
        binding.rangeslider.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed
        }
    }

    private fun selectedFilterDefault(){
        binding.JTItems.check(binding.JTAll.id)
        Log.d("ádfaaw", "selectedFilterDefault hoạt động")
    }
}