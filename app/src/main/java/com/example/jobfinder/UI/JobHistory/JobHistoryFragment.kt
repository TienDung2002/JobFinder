package com.example.jobfinder.UI.JobHistory

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentJobHistoryBinding

class JobHistoryFragment(private val animationView: LottieAnimationView) : Fragment() {

    private val viewModel: JobHistoryViewModel by viewModels()
    private lateinit var binding: FragmentJobHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animationView.visibility= View.VISIBLE

        animationView.visibility= View.GONE


    }
}