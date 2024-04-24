package com.example.jobfinder.UI.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.jobfinder.UI.JobPosts.JobpostsActivity
import com.example.jobfinder.UI.PostedJob.PostedJobActivity
import com.example.jobfinder.UI.PostedJob.PostedJobViewModel
import com.example.jobfinder.UI.Wallet.WalletActivity
import com.example.jobfinder.databinding.FragmentHomeBuserBinding

class HomeFragmentBuser : Fragment() {
    private lateinit var binding: FragmentHomeBuserBinding
    private val jobViewModel: PostedJobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobViewModel.fetchPostedJobs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBuserBinding.inflate(inflater, container, false)


        // mở activity wallet
        binding.walletBtn.setOnClickListener{
            startActivity(Intent(requireContext(), WalletActivity::class.java))
        }

        // open post job activity
        binding.recruitBtn.setOnClickListener{
            startActivity(Intent(requireContext(), JobpostsActivity::class.java))
        }

        binding.recruitPostedBtn.setOnClickListener{
            startActivity(Intent(requireContext(), PostedJobActivity::class.java))
        }

        return binding.root
    }

}