package com.example.jobfinder.UI.Admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.Datas.Model.idAndRole
import com.example.jobfinder.R
import com.example.jobfinder.UI.ForgotPassword.ForgotPassActivity
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityAdminHomeBinding
import com.example.jobfinder.databinding.ActivityLoginAdminBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase

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

                val test = true
                if (test) {
                    val intent = Intent(this, AdminHomeActivity::class.java)
                    binding.animationView.visibility = View.GONE
                    startActivityForResult(intent, LOGIN_REQUEST_CODE)
                }
            }

        }
    }

    private fun checkRole(role: String, userType: String){
        if (role == userType){
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            startActivity(Intent(this, HomeActivity::class.java))
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