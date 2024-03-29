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

    //kiểm tra ngày B có trước ngày A không
    fun compareDates(dateA: String, dateB: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        if(dateA.isNotEmpty() && dateB.isNotEmpty()) {
            // Chuyển đổi chuỗi ngày thành đối tượng Date
            val dateObjA = dateFormat.parse(dateA)
            val dateObjB = dateFormat.parse(dateB)

            // So sánh ngày
            if (dateObjB != null && dateObjA != null) {
                return !dateObjB.before(dateObjA)
            }
        }
        return false
    }

    //đếm số ngày từ ngày A đến ngày B
    fun countDaysBetweenDates(dateA: String, dateB: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            // Chuyển đổi chuỗi ngày thành đối tượng Date
            val startDate = dateFormat.parse(dateA)
            val endDate = dateFormat.parse(dateB)

            // Tính số lượng miliseconds giữa hai ngày
            val diffInMillis = endDate.time - startDate.time

            // Chuyển đổi số miliseconds thành số ngày và trả về kết quả
            return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            // Xử lý nếu có lỗi xảy ra trong quá trình chuyển đổi ngày
            e.printStackTrace()
        }

        return -1 // Trả về -1 nếu có lỗi xảy ra
    }

    // lấy ngày theo dang dd/MM/yyyy từ String date
    fun getDateFromString(dateTimeString: String): String? {
        val parts = dateTimeString.split(" ")
        if (parts.isNotEmpty()) {
            return parts[0] // Trả về phần tử đầu tiên, chứa ngày tháng năm
        }
        return null
    }

    // lấy giờ từ string date
    fun getTimeFromDate(date: Date): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(date)
    }

    fun multiplyStrings(string1: String, string2: String): String {
        // Chuyển đổi chuỗi thành số float
        val number1 = string1.toFloatOrNull() ?: 0f
        val number2 = string2.toFloatOrNull() ?: 0f

        val result = number1 * number2

        return result.toString()
    }

    //kiểm tra xem A có lớn hơn B không
    fun compareFloatStrings(strA: String, strB: String): Boolean {
        val floatA = strA.toFloatOrNull()
        val floatB = strB.toFloatOrNull()

        if (floatA != null && floatB != null) {
            return floatA > floatB
        }
        return false
    }


}