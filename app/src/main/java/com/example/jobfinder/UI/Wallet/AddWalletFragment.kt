package com.example.jobfinder.UI.Wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentAddWalletBinding


class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private var pickedColor = "blue"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group)

        binding.chooseColorBlue.setOnClickListener{
            pickedColor = "blue"
            binding.apply {
                addWalletCardPreviewBg.setBackgroundResource(R.drawable.wallet_blue_bg)
                imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group)
            }
        }
        binding.chooseColorRed.setOnClickListener{
            pickedColor = "red"
            binding.apply {
                addWalletCardPreviewBg.setBackgroundResource(R.drawable.wallet_red_bg)
                imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group_white_a700)
            }
        }
        binding.chooseColorGreen.setOnClickListener{
            pickedColor = "green"
            binding.apply {
                addWalletCardPreviewBg.setBackgroundResource(R.drawable.wallet_green_bg)
                imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group_white_a700_170x319)
            }
        }

    }
}
