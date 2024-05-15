package com.example.jobfinder.Utils

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.Datas.Model.idAndRole
import com.example.jobfinder.UI.FindNewJobs.FindNewJobViewModel
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

        var result = -1 // Khởi tạo giá trị mặc định nếu có lỗi xảy ra

        try {
            val startDate = dateFormat.parse(dateA)
            val endDate = dateFormat.parse(dateB)

            val diffInMillis = endDate.time - startDate.time
            result = (diffInMillis / (1000 * 60 * 60 * 24)).toInt() + 1
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result // Di chuyển lệnh return ra khỏi khối try-catch
    }

    // lấy ngày theo dang dd/MM/yyyy từ String date
    fun getDateFromString(dateTimeString: String): String? {
        val parts = dateTimeString.split(" ")
        if (parts.isNotEmpty()) {
            return parts[0] // Trả về phần tử đầu tiên, chứa ngày tháng năm
        }
        return null
    }
    // Chuyển chuỗi ngày tháng từ String thành kiểu Date - Lấy dd/MM/yyyy
    fun convertStringToDATE(dateTimeString: String): Date? {
        val parts = dateTimeString.split(" ")
        if (parts.isNotEmpty()) {
            val dateString = parts[0]
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return format.parse(dateString)
        }
        return null
    }


    @SuppressLint("DefaultLocale")
    fun formatIntToTime(hours: Int): String {
        val totalMinutes = hours * 60
        val hoursPart = totalMinutes / 60
        val minutesPart = totalMinutes % 60
        return String.format("%02d:%02d", hoursPart, minutesPart)
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
            return floatA >= floatB
        }
        return false
    }

    fun getStatus(startTime: String, endTime: String, empAmount: String, recruitedEmp: String): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val startDate = sdf.parse(startTime)
            val endDate = sdf.parse(endTime)
            val today = sdf.parse(currentDate)

            val empAmountInt = empAmount.toIntOrNull()
            val recruitedEmpInt = recruitedEmp.toIntOrNull()

            if (empAmountInt != null && recruitedEmpInt != null) {
                if (recruitedEmpInt >= empAmountInt) {
                    return "closed"
                }
            }
            return when {
                today.after(endDate) -> "closed"
                today.before(startDate) -> "recruiting"
                else -> "working"
            }
        }catch (e: Exception) {
            e.printStackTrace()
            return "null"
        }
    }


    fun isTimeBeforeOneHour(timeA: String, timeB: String): Boolean {
        if(timeA.isNotEmpty() && timeB.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                sdf.isLenient = false

                // Parse thời gian từ chuỗi
                val dateA = sdf.parse(timeA)
                val dateB = sdf.parse(timeB)

                // Sử dụng Calendar để tính toán chênh lệch thời gian
                val calendarA = Calendar.getInstance().apply { time = dateA }
                val calendarB = Calendar.getInstance().apply { time = dateB }

                // Xóa thông tin liên quan đến ngày
                calendarA.set(Calendar.YEAR, 0)
                calendarA.set(Calendar.MONTH, 0)
                calendarA.set(Calendar.DAY_OF_MONTH, 0)

                calendarB.set(Calendar.YEAR, 0)
                calendarB.set(Calendar.MONTH, 0)
                calendarB.set(Calendar.DAY_OF_MONTH, 0)

                // Chênh lệch thời gian tính bằng millisecond
                val timeDifference = calendarB.timeInMillis - calendarA.timeInMillis

                // Chuyển đổi 1 giờ thành millisecond
                val oneHourInMillis = 3600000 // 1 giờ = 60 phút * 60 giây * 1000 milliseconds

                // Kiểm tra xem thời gian chênh lệch có lớn hơn hoặc bằng 1 tiếng không
                return timeDifference >= oneHourInMillis
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun calculateHourDifference(timeA: String, timeB: String): Float {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        var result = 0f // Khởi tạo giá trị mặc định nếu có lỗi xảy ra

        try {
            val dateA = sdf.parse(timeA)
            val dateB = sdf.parse(timeB)

            val calendarA = Calendar.getInstance().apply { time = dateA }
            val calendarB = Calendar.getInstance().apply { time = dateB }

            val timeDifference = calendarB.timeInMillis - calendarA.timeInMillis

            val hours = timeDifference / (1000 * 60 * 60).toFloat()
            val minutes = (timeDifference % (1000 * 60 * 60)).toFloat() / (1000 * 60)

            result = hours + (minutes / 60)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return result // Di chuyển lệnh return ra khỏi khối try-catch
    }

    fun getCurrentUserId(): String? {
        val currentUser = auth.currentUser
        return currentUser?.uid
    }

    fun getUsernameFromUserId(userId: String, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("UserBasicInfo").child(userId)

        database.child("name").get()
            .addOnSuccessListener { snapshot ->
                val username = snapshot.getValue(String::class.java)
                callback(username)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun formatLabelHoursSlider(value: Float): String {  // Ví dụ convert 1.5 sẽ trở thành "01:30"
        val hours = value.toInt()
        val minutes = ((value - hours) * 60).toInt()
        return String.format("%02d:%02d", hours, minutes)
    }
}