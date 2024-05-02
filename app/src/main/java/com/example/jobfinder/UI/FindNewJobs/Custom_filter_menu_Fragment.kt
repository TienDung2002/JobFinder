package com.example.jobfinder.UI.FindNewJobs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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


        defaultFilter()

        val jtButtons = listOf(binding.JTAll, binding.JTAtoZ)
        val recNameButtons = listOf(binding.recAll, binding.recAtoZ)
        val postedTimeButtons = listOf(binding.PTAnytime, binding.PTNewest, binding.PTThisMonth)
        val workShiftButtons = listOf(binding.WSAll, binding.WSMorning, binding.WSAfternoon)

//        jtButtons.forEach { button ->
//            button.setOnClickListener {
//                handleButtonSelection(jtButtons, button)
//            }
//        }

        binding.JTAll.setOnClickListener {
            Toast.makeText(requireContext(), "Button clicked", Toast.LENGTH_SHORT).show()
        }
//        recNameButtons.forEach { button ->
//            button.setOnClickListener {
//                handleButtonSelection(recNameButtons, button)
//            }
//        }
//
//        postedTimeButtons.forEach { button ->
//            button.setOnClickListener {
//                handleButtonSelection(postedTimeButtons, button)
//            }
//        }
//
//        workShiftButtons.forEach { button ->
//            button.setOnClickListener {
//                handleButtonSelection(workShiftButtons, button)
//            }
//        }

        binding.resetBtn.setOnClickListener {

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //tương tác với UI sau khi onCreateView
        super.onViewCreated(view, savedInstanceState)



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

    private fun handleButtonSelection(buttons: List<Button>, selectedButton: Button) {
        // Cập nhật background của Button được chọn
        selectedButton.setBackgroundResource(R.drawable.custom_filter_btn_selected)

        // Lặp qua tất cả các Button trong danh sách
        for (button in buttons) {
            // Nếu Button không phải là Button được chọn, đặt background về mặc định
            if (button != selectedButton) {
                button.setBackgroundResource(R.drawable.custom_filter_btn_default)
            }
        }
    }

    private fun defaultFilter() {
        Log.d("adsfasdf", "đang hoạt động")
        binding.JTAll.setBackgroundResource(R.drawable.custom_filter_btn_selected)
        binding.recAll.setBackgroundResource(R.drawable.custom_filter_btn_selected)
        binding.PTAnytime.setBackgroundResource(R.drawable.custom_filter_btn_selected)
        binding.WSAll.setBackgroundResource(R.drawable.custom_filter_btn_selected)
    }


}