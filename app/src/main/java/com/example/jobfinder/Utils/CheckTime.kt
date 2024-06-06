package com.example.jobfinder.Utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.*

object CheckTime {

    //kiểm tra xem giờ điểm danh có bắt đàu trước giờ bắt đầu không
    fun checkTimeBefore(checkInTime: String, startTime: String): Boolean {
        if (checkInTime.isEmpty() || startTime.isEmpty()) {
            return false
        } else {
            val checkInDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            try {
                // Parse checkInTime để lấy ra thời gian dưới dạng Date
                val checkInDate = checkInDateFormat.parse(checkInTime)
                if(checkInDate!= null) {

                    // Chuyển đổi checkInTime về dạng "HH:mm"
                    val checkInTimeStr = timeFormat.format(checkInDate)

                    // Parse startTime để so sánh
                    val checkInTimeParsed = timeFormat.parse(checkInTimeStr)
                    if(checkInTimeParsed != null) {
                        val startTimeParsed = timeFormat.parse(startTime)


                        // So sánh checkInTime và startTime
                        return checkInTimeParsed.before(startTimeParsed) || checkInTimeParsed == startTimeParsed
                    }
                }
                return false
            } catch (e: Exception) {
                // Xử lý nếu có lỗi khi parse
                e.printStackTrace()
                return false
            }
        }
    }

    fun calculateMinuteDiff(startTime: String, endTime: String): Int {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        return try {
            // Parse startTime và endTime chỉ với phần giờ và phút
            val startFullDate = timeFormat.parse(startTime)!!

            // Phân tích endTime để lấy phần giờ và phút
            val endHourMinute = endTime.substring(11, 16)
            val endHourMinuteDate = timeFormat.parse(endHourMinute)!!

            // Tính sự chênh lệch ở dạng milliseconds
            val diffInMillis = endHourMinuteDate.time - startFullDate.time

            // Chuyển đổi từ milliseconds sang phút
            val diffInMinutes = (diffInMillis / (1000 * 60)).toInt()

            diffInMinutes
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }



}