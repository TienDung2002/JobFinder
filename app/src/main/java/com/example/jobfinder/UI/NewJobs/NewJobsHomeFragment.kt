package com.example.jobfinder.UI.NewJobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentNewJobsHomeBinding


class NewJobsHomeFragment : Fragment() {
    lateinit var binding: FragmentNewJobsHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewJobsHomeBinding.inflate(layoutInflater, container, false)




        return binding.root
    }

}