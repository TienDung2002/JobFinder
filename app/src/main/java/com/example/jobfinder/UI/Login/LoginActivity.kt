package com.example.jobfinder.UI.Login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Datas.Model.idAndRole
import com.example.jobfinder.R
import com.example.jobfinder.UI.ForgotPassword.ForgotPassActivity
import com.example.jobfinder.UI.Home.HomeActivity
import com.example.jobfinder.UI.Notifications.NotificationsActivity
import com.example.jobfinder.UI.Register.RecruiterRegisterActivity
import com.example.jobfinder.UI.Register.SeekerRegisterActivity
import com.example.jobfinder.Utils.PasswordToggleState
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var isPassVisible = PasswordToggleState(false)
    private lateinit var auth: FirebaseAuth
    private val LOGIN_REQUEST_CODE = 100 // Đặt một mã request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()


        // gọi hàm đổi icon và ẩn hiện password
        VerifyField.changeIconShowPassword(binding.passwordTextInputLayout, isPassVisible, binding.userPassLogin)


        // Lấy role từ bên select role
        val userType = intent.getStringExtra("user_type")


        // Mở register
        binding.openRegisterActi.setOnClickListener{
            if (PreventDoubleClick.checkClick()) {
                if (userType == "NUser") {
                    val intent = Intent(this, SeekerRegisterActivity::class.java)
                    startActivity(intent)
                } else if (userType == "BUser") {
                    val intent = Intent(this, RecruiterRegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }


        // Hiển thị tiêu đề dựa vào role đã chọn
        if (userType == "NUser") {
            binding.titleLogin1.setText(R.string.welcome_seek1)
            binding.titleLogin2.setText(R.string.welcome_seek2)
        } else {
            binding.titleLogin1.setText(R.string.welcome_rec1)
            binding.titleLogin2.setText(R.string.welcome_rec2)
        }


        // Xác nhận để Login
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
                auth.signInWithEmailAndPassword(emailInput, passInput).addOnCompleteListener {
                    if(it.isSuccessful){
                        val uid = auth.currentUser?.uid
                        FirebaseDatabase.getInstance().getReference("UserRole").child(uid.toString()).get().addOnSuccessListener {
                            val data: idAndRole? = it.getValue(idAndRole::class.java)
                            if (data != null) {
                                checkRole(data.role.toString(), userType.toString())
                            }
                            // ẩn loading khi đăng nhập hoàn tất
                            binding.animationView.visibility = View.GONE
                        }.addOnFailureListener{
                            Log.e("Login button", "Something wrong while getting data", it)
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    binding.animationView.visibility = View.GONE
                    checkToAutoFocus(isEmailValid, isPassValid)
                }
            } else {
                checkToAutoFocus(isEmailValid, isPassValid)
            }
        }


        // Quên mật khẩu
        binding.moveToForgotBtn.setOnClickListener{
            if (PreventDoubleClick.checkClick()) {
                val intent = Intent(this, ForgotPassActivity::class.java)
                startActivity(intent)
            }
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


    // Xử lý kết quả từ Homeactivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Đăng nhập thành công, kết thúc cả SelectRoleActivity và LoginActivity
            finish()
        }
    }

}