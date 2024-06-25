package com.example.jobfinder.UI.Admin.Login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.R
import com.example.jobfinder.UI.Admin.Home.AdminHomeActivity
import com.example.jobfinder.UI.ForgotPassword.ForgotPassActivity
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityLoginAdminBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class AdminLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginAdminBinding
    private lateinit var auth: FirebaseAuth
    private val LOGIN_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()

        // Lấy usertype từ select role gửi tới
        val userType = intent.getStringExtra("user_type")

        // Quên mật khẩu
        binding.moveToForgotBtn.setOnClickListener {
            if (PreventDoubleClick.checkClick()) {
                val intent = Intent(this, ForgotPassActivity::class.java)
                startActivity(intent)
            }
        }

        // login
        binding.btnLogin.setOnClickListener {
            // chạy animation loading
            binding.animationView.visibility = View.VISIBLE

            val emailInput = binding.userEmailLogin.text.toString().trim()
            val passInput = binding.userPassLogin.text.toString()
            val isEmailValid = emailInput.isNotEmpty() && VerifyField.isValidEmail(emailInput)
            val isPassValid = VerifyField.isValidPassword(passInput)

            binding.userEmailLogin.error = if (isEmailValid) null else {
                binding.animationView.visibility = View.GONE
                getString(R.string.error_invalid_email)
            }

            binding.userPassLogin.error = if (isPassValid) null else {
                binding.animationView.visibility = View.GONE
                getString(R.string.error_pass)
            }

            if (isEmailValid && isPassValid) {
//                auth.signInWithEmailAndPassword(emailInput, passInput).addOnCompleteListener {
//
//
//                }.addOnFailureListener {exception ->
//                    // kiểm tra nếu lỗi là do mật khẩu sai
//                    if (exception is FirebaseAuthInvalidCredentialsException) {
//                        Toast.makeText(applicationContext, getString(R.string.error_wrong_passwordOrUsername), Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(applicationContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
//                    }
//                    binding.animationView.visibility = View.GONE
//                    checkToAutoFocus(isEmailValid, isPassValid)
//                }
//            } else {
//                checkToAutoFocus(isEmailValid, isPassValid)
//            }


                checkRole("Admin", userType.toString())
                binding.animationView.visibility = View.GONE

            }

        }
    }


    private fun checkRole(role: String, userType: String){
        if (role == userType){
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            startActivity(Intent(this, AdminHomeActivity::class.java))
            finish()
        }else {
            Toast.makeText(applicationContext, getString(R.string.wrong_role), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<TextInputEditText>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.userEmailLogin)
                    1 -> invalidFields.add(binding.userPassLogin)
                }
            }
        }
        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }

    // Xử lý kết quả từ Homeactivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Đăng nhập thành công, kết thúc cả SelectRoleActivity và LoginActivity
            finish()
        }
    }





}