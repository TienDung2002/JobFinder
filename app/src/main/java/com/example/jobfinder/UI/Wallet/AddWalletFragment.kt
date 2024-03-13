package com.example.jobfinder.UI.Wallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jobfinder.Datas.Model.WalletRowModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.FragmentAddWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddWalletFragment : Fragment() {
    private lateinit var binding: FragmentAddWalletBinding
    private var pickedColor = "blue"
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //firebase
        auth = FirebaseAuth.getInstance()
        binding.imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group)

        binding.addCardBtn.setOnClickListener{
            val bankName = binding.addWalletBankEditTxt.text.toString().trim()
            val cardNumber = binding.addWalletCardNumEditTxt.text.toString().trim()
            val expDate = binding.addWalletExpDateEditTxt.text.toString().trim()

            val isValidBank= VerifyField.isEmpty(bankName)
            val isValidCardNumber= VerifyField.isValidCardNumber(cardNumber)
            val isValidExpDate= VerifyField.isEmpty(expDate)

            binding.addWalletBankEditTxt.error = if (isValidBank) null else getString(R.string.error_invalid_bank)
            binding.addWalletCardNumEditTxt.error = if (isValidCardNumber) null else getString(R.string.error_invalid_card_num)
            binding.addWalletExpDateEditTxt.error = if (isValidExpDate) null else getString(R.string.error_invalid_exp_date)

            if(isValidBank && isValidCardNumber &&isValidExpDate){
                val cardColor = pickedColor
                val uid = auth.currentUser?.uid
                val cardId= FirebaseDatabase.getInstance().getReference("Wallet").child(uid.toString()).push().key
                val newWalletRow = WalletRowModel(cardId,bankName, "0.0", cardNumber, expDate, cardColor)
                FirebaseDatabase.getInstance().getReference("Wallet").child(uid.toString()).child(cardId.toString()).setValue(newWalletRow).addOnCompleteListener() {
                    if(it.isSuccessful){
                        Toast.makeText(context, getString(R.string.add_card_success), Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, WalletActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }else {
                        Toast.makeText(context, getString(R.string.add_card_fail), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                checkToAutoFocus(isValidBank, isValidCardNumber,isValidExpDate)
            }
        }

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

    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<EditText>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.addWalletBankEditTxt)
                    1 -> invalidFields.add(binding.addWalletCardNumEditTxt)
                    2 -> invalidFields.add(binding.addWalletExpDateEditTxt)
                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }

}