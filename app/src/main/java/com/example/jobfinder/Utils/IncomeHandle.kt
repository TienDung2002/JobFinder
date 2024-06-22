package com.example.jobfinder.Utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.jobfinder.Datas.Model.IncomeByJobTypeModel
import com.example.jobfinder.Datas.Model.IncomeModel
import com.example.jobfinder.Datas.Model.NUserWorkHour
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object IncomeHandle{

    @SuppressLint("NewApi")
    fun calculateWeeklyIncome(incomes: MutableList<IncomeModel>, year: Int, month: Int): Map<Int, Double> {
        // Định dạng ngày
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Lấy ngày đầu và ngày cuối của tháng
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val lastDayOfMonth = YearMonth.of(year, month).atEndOfMonth()

        // Khởi tạo một Map để lưu kết quả tổng thu nhập theo tuần
        val weeklyTotals = mutableMapOf<Int, Double>()

        // Biến để theo dõi số tuần
        var currentWeek = 1

        // Ngày bắt đầu tuần đầu tiên là ngày 1 của tháng
        var startOfWeek = firstDayOfMonth

        // Duyệt qua từng tuần trong tháng
        while (startOfWeek.isBefore(lastDayOfMonth) || startOfWeek == lastDayOfMonth) {
            // Tìm ngày cuối của tuần hiện tại
            val endOfWeek = if (currentWeek == 1) {
                // Nếu là tuần đầu tiên, kết thúc vào Chủ nhật
                startOfWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            } else {
                // Ngày bắt đầu tuần tiếp theo là thứ 2
                val nextMonday = startOfWeek.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                // Nếu ngày bắt đầu của tuần tiếp theo là ngày cuối tháng, kết thúc là ngày cuối tháng
                if (nextMonday.isAfter(lastDayOfMonth)) lastDayOfMonth else nextMonday.minusDays(1)
            }

            // Tính tổng thu nhập của tuần
            val weeklyIncome = incomes
                .filter { income ->
                    val incomeDate = LocalDate.parse(income.incomeDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    !incomeDate.isBefore(startOfWeek) && !incomeDate.isAfter(endOfWeek)
                            && incomeDate.year == year && incomeDate.monthValue == month
                }
                .sumOf { it.incomeAmount?.toDoubleOrNull() ?: 0.0 }

            // Lưu tổng thu nhập vào Map theo số tuần
            weeklyTotals[currentWeek] = weeklyIncome

            // Di chuyển tới ngày bắt đầu của tuần tiếp theo
            startOfWeek = endOfWeek.plusDays(1)

            // Tăng số tuần
            currentWeek++
        }

        return weeklyTotals
    }

    fun calculateIncomeByJobType(list: MutableList<IncomeByJobTypeModel>): Map<Int, Double> {
        val jobTypeIncomeMap = mutableMapOf<Int, Double>()

        for (item in list) {
            val jobTypeString = item.jobType ?: continue
            val jobTypeInt = jobTypeString.toIntOrNull() ?: continue
            val incomeAmount = item.incomeAmount?.toDoubleOrNull() ?: continue

            jobTypeIncomeMap[jobTypeInt] = jobTypeIncomeMap.getOrDefault(jobTypeInt, 0.0) + incomeAmount
        }

        return jobTypeIncomeMap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateWorkHoursByDay(
        list: MutableList<NUserWorkHour>,
        year: Int,
        month: Int
    ): Map<Int, Double> {
        val workHoursByDay = mutableMapOf<Int, Double>()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Khởi tạo workHoursByDay với giờ làm = 0 cho tất cả các ngày trong tháng
        val daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth()
        for (dayOfMonth in 1..daysInMonth) {
            workHoursByDay[dayOfMonth] = 0.0
        }

        // Xử lý dữ liệu từ danh sách list
        for (item in list) {
            val workDate = item.workDate ?: continue
            val workTime = item.workTime?.toDoubleOrNull() ?: 0.0

            try {
                val date = LocalDate.parse(workDate, formatter)

                // Chỉ xử lý các ngày trong tháng và năm đã cho
                if (date.year == year && date.monthValue == month) {
                    val dayOfMonth = date.dayOfMonth

                    // Cộng dồn số giờ làm cho ngày trong tháng
                    workHoursByDay[dayOfMonth] = workHoursByDay[dayOfMonth]?.plus(workTime) ?: workTime
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ nếu không thể phân tích chuỗi workDate thành LocalDate
                e.printStackTrace()
            }
        }

        return workHoursByDay
    }


}