package com.example.jobfinder.UI.UsersProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.UI.ForgotPassword.ForgotPassFragment
import com.example.jobfinder.UI.SplashScreen.SelectRoleActivity
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityForgotPassBinding
import com.example.jobfinder.databinding.ActivitySettingsBinding
import com.example.jobfinder.databinding.FragmentSettingsMenuBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class SettingsMenuFragment : Fragment() {
    private lateinit var binding: FragmentSettingsMenuBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsMenuBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.changePassword.setOnClickListener(){

            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.password_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
            val title = dialogView.findViewById<TextView>(R.id.dialog_title)
            val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancle)
            val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirm)
            val currentPasswordEditText = dialogView.findViewById<EditText>(R.id.current_pass)
            val forgotPassword = dialogView.findViewById<TextView>(R.id.forgot_pass)

            title.setText(getString(R.string.Confirm_change_pass))

            val alertDialog = builder.create()
            alertDialog.show()
            cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }
            confirmButton.setOnClickListener {
                val currentPassword = currentPasswordEditText.text.toString()
                val isvalid_pass = VerifyField.isValidPassword(currentPassword)
                currentPasswordEditText.error = if (isvalid_pass) null else getString(R.string.error_pass)

                if (isvalid_pass){
                    val userId = auth.currentUser
                    userId?.let { currentUser ->
                        val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword)
                        currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                val activity = requireActivity() as SettingsActivity
                                activity.replaceFragment(ChangePasswordFragment())
                                alertDialog.dismiss()
                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    "Re-authentication failed. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            forgotPassword.setOnClickListener(){
                val intent = Intent(requireContext(), ProfileForgotPasswordActivity::class.java)
                startActivity(intent)
                alertDialog.dismiss()
            }
        }

        binding.deleteAccount.setOnClickListener(){
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.password_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
            val title = dialogView.findViewById<TextView>(R.id.dialog_title)
            val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancle)
            val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirm)
            val currentPasswordEditText = dialogView.findViewById<EditText>(R.id.current_pass)
            val forgotPassword = dialogView.findViewById<TextView>(R.id.forgot_pass)
            title.setText(getString(R.string.Confirm_delete_account))

            val alertDialog = builder.create()
            alertDialog.show()
            cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }
            confirmButton.setOnClickListener {
                val currentPassword = currentPasswordEditText.text.toString()
                val isvalid_pass = VerifyField.isValidPassword(currentPassword)
                currentPasswordEditText.error = if (isvalid_pass) null else getString(R.string.error_pass)

                if (isvalid_pass){
                    val userId = auth.currentUser
                    userId?.let { currentUser ->
                        val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword)
                        currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                currentUser.delete().addOnCompleteListener { deleteTask ->
                                    if (deleteTask.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.account_deleted),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        alertDialog.dismiss()
                                        val intent = Intent(requireContext(), SelectRoleActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        requireActivity().finishAffinity()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.delete_account_failed),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    R.string.re_authen_failed,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }




            }
            forgotPassword.setOnClickListener(){
                val intent = Intent(requireContext(), ProfileForgotPasswordActivity::class.java)
                startActivity(intent)
                alertDialog.dismiss()
            }
        }

        binding.backbtn.setOnClickListener(){
            val resultIntent = Intent()
            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
            requireActivity().finish()
        }
    }






}