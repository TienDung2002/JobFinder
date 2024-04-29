package com.example.jobfinder.UI.FindNewJobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentCustomFilterMenuBinding
import com.example.jobfinder.databinding.FragmentForgotPassBinding


class Custom_filter_menu_Fragment : Fragment() {
    private lateinit var binding: FragmentCustomFilterMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomFilterMenuBinding.inflate(inflater, container, false)


        return binding.root
    }


}