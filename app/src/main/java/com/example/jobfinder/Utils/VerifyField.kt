package com.example.jobfinder.Utils
import android.text.method.PasswordTransformationMethod
import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


data class PasswordToggleState(var isPassVisible: Boolean)

object VerifyField {

    //thay đổi icon
    fun changeIconShowPassword(
        inputLayout: TextInputLayout,
        passwordToggleState: PasswordToggleState,
        inputEditText: TextInputEditText
    ) {
        inputLayout.setEndIconOnClickListener {
            passwordToggleState.isPassVisible = !passwordToggleState.isPassVisible
            togglePasswordVisible(inputEditText, passwordToggleState.isPassVisible)
        }
    }
    // ẩn hiện password
    fun togglePasswordVisible(editText: TextInputEditText, isPassVisible: Boolean) {
        if (isPassVisible) editText.transformationMethod = null // transformationMethod = null thì xem dc text
        else editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.text?.let { editText.setSelection(it.length) } //di chuyển con trỏ về cuối text
    }

    // check email hợp lệ
    fun isValidEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    // check độ dài số điện thoại
     fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty() && phoneNumber.length == 10
    }

}