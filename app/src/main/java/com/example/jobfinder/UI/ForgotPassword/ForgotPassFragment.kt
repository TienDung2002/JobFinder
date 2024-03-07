package com.example.jobfinder.UI.ForgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jobfinder.R
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.FragmentForgotPassBinding
import com.google.firebase.auth.FirebaseAuth


class ForgotPassFragment : Fragment() {
    private lateinit var binding: FragmentForgotPassBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPassBinding.inflate(inflater, container, false)


        // nút reset password
        binding.btnReset.setOnClickListener{
            if (PreventDoubleClick.checkClick()) {
                val emailInput = binding.emailInput.text.toString()
                val isValidEmail = VerifyField.isValidEmail(emailInput) && emailInput.isNotEmpty()

                if (isValidEmail) {
                    binding.emailInput.error = null
                    binding.forgotTitle.setText(R.string.FP_Title2)
                    binding.subTitle1.setText(R.string.FP_subTitle2)
                    binding.image.setImageResource(R.drawable.ic_checkemail)
                    binding.subTitle2.setText(emailInput)

                    sendEmailResetPass(emailInput)
                    Toast.makeText(requireContext(), getString(R.string.FP_toast), Toast.LENGTH_SHORT).show()
                } else {
                    binding.emailInput.error = getString(R.string.error_invalid_email)
                    // trỏ focus vào trường luôn
                    binding.emailInput.requestFocus()
                }
            }
        }

        return binding.root
    }

    private fun sendEmailResetPass(email: String){
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
    }
}