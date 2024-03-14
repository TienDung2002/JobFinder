package com.example.jobfinder.UI.Wallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
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
    private var yearChoose= false
    private var monthChoose=false

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

        binding.txtYear.setOnClickListener {
            if(!yearChoose){
                binding.txtYear.text = "24"
                binding.txtYear.error= null}
            txtYearSelected(it)
            yearChoose = true
        }
        binding.txtMonth.setOnClickListener{
            if(!monthChoose){
                binding.txtMonth.text = "01"
                binding.txtMonth.error = null
            }
            txtMonthSelected(it)
            monthChoose = true
        }

        binding.addCardBtn.setOnClickListener{


            val bankName = binding.addWalletBankEditTxt.text.toString().trim()
            val cardNumber = binding.addWalletCardNumEditTxt.text.toString().trim()
            val expYear = binding.txtYear.text.toString()
            val expMonth = binding.txtMonth.text.toString()
            val expDate = "$expYear/$expMonth"

            val isValidBank= VerifyField.isEmpty(bankName)
            val isValidCardNumber= VerifyField.isValidCardNumber(cardNumber)
            val isValidExpDate= VerifyField.isEmpty(expDate)
            val isValidYear= yearChoose
            val isValidMonth= monthChoose


            binding.addWalletBankEditTxt.error = if (isValidBank) null else getString(R.string.error_invalid_bank)
            binding.addWalletCardNumEditTxt.error = if (isValidCardNumber) null else getString(R.string.error_invalid_card_num)
            binding.txtYear.error=if(isValidYear)null else getString(R.string.no_choose_year)
            binding.txtMonth.error=if(isValidMonth)null else getString(R.string.no_choose_month)

            if(isValidBank && isValidCardNumber &&isValidExpDate && yearChoose&& monthChoose){
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
        binding.chooseColorPink.setOnClickListener{
            pickedColor = "pink"
            binding.apply {
                addWalletCardPreviewBg.setBackgroundResource(R.drawable.wallet_pink_bg)
                imageMainAddWalletImage.setImageResource(R.drawable.img_mask_group)
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
                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }

    private fun txtYearSelected(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.year_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.year_24 -> {
                    // Xử lý khi chọn năm 24
                    binding.txtYear.text = "24"
                    true
                }
                R.id.year_25 -> {
                    // Xử lý khi chọn năm 25
                    binding.txtYear.text = "25"
                    true
                }
                R.id.year_26 -> {
                    // Xử lý khi chọn năm 26
                    binding.txtYear.text = "26"
                    true
                }
                R.id.year_27 -> {
                    // Xử lý khi chọn năm 27
                    binding.txtYear.text = "27"
                    true
                }
                R.id.year_28 -> {
                    // Xử lý khi chọn năm 28
                    binding.txtYear.text = "28"
                    true
                }
                R.id.year_29 -> {
                    // Xử lý khi chọn năm 29
                    binding.txtYear.text = "29"
                    true
                }
                R.id.year_30 -> {
                    // Xử lý khi chọn năm 30
                    binding.txtYear.text = "30"
                    true
                }
                R.id.year_31 -> {
                    // Xử lý khi chọn năm 31
                    binding.txtYear.text = "31"
                    true
                }
                R.id.year_32 -> {
                    // Xử lý khi chọn năm 32
                    binding.txtYear.text = "32"
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun txtMonthSelected(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.month_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.month_01 -> {
                    // Xử lý khi chọn tháng 01
                    binding.txtMonth.text = "01"
                    true
                }
                R.id.month_02 -> {
                    // Xử lý khi chọn tháng 02
                    binding.txtMonth.text = "02"
                    true
                }
                R.id.month_03 -> {
                    // Xử lý khi chọn tháng 03
                    binding.txtMonth.text = "03"
                    true
                }
                R.id.month_04 -> {
                    // Xử lý khi chọn tháng 04
                    binding.txtMonth.text = "04"
                    true
                }
                R.id.month_05 -> {
                    // Xử lý khi chọn tháng 05
                    binding.txtMonth.text = "05"
                    true
                }
                R.id.month_06 -> {
                    // Xử lý khi chọn tháng 06
                    binding.txtMonth.text = "06"
                    true
                }
                R.id.month_07 -> {
                    // Xử lý khi chọn tháng 07
                    binding.txtMonth.text = "07"
                    true
                }
                R.id.month_08 -> {
                    // Xử lý khi chọn tháng 08
                    binding.txtMonth.text = "08"
                    true
                }
                R.id.month_09 -> {
                    // Xử lý khi chọn tháng 09
                    binding.txtMonth.text = "09"
                    true
                }
                R.id.month_10 -> {
                    // Xử lý khi chọn tháng 10
                    binding.txtMonth.text = "10"
                    true
                }
                R.id.month_11 -> {
                    // Xử lý khi chọn tháng 11
                    binding.txtMonth.text = "11"
                    true
                }
                R.id.month_12 -> {
                    // Xử lý khi chọn tháng 12
                    binding.txtMonth.text = "12"
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}