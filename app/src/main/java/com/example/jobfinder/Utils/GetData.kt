package com.example.jobfinder.Utils

import com.example.jobfinder.Datas.Model.idAndRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Date

// Cách dùng getUserRole:
// Khai báo giá trị mặc định cho 1 biến global ở Activity cần check :  private var userRole: String = ""
// Gọi hàm và gán giá trị thu được từ callback cho biến vừa khai báo và sử dụng:
//      GetData.getUserRole { role ->
//          role?.let {
//              userRole = it
//          // biến userRole sẽ mang kết quả thu được và chỉ sử dụng được trong callback này (tham khảo home)
//          }
//      }

object GetData {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun getUserRole(callback: (String?) -> Unit) {
        val uid = auth.currentUser?.uid
        FirebaseDatabase.getInstance().getReference("UserRole").child(uid.toString()).get()
            .addOnSuccessListener { snapshot ->
                val data: idAndRole? = snapshot.getValue(idAndRole::class.java)
                val userRole = data?.role
                callback(userRole)
            }
            .addOnFailureListener {
                callback("null string") // Trả về null trong trường hợp có lỗi
            }
    }

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    fun convertStringToDate(dateString: String?): Date? {
        if (dateString.isNullOrEmpty()) {
            return null
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        try {
            return dateFormat.parse(dateString)
        } catch (e: ParseException) {
            // Xử lý nếu chuỗi ngày không đúng định dạng
            e.printStackTrace()
        }
        return null
    }
    fun getDateFromString(dateTimeString: String): String? {
        val parts = dateTimeString.split(" ")
        if (parts.isNotEmpty()) {
            return parts[0] // Trả về phần tử đầu tiên, chứa ngày tháng năm
        }
        return null
    }

    fun getTimeFromDate(date: Date): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(date)
    }

}