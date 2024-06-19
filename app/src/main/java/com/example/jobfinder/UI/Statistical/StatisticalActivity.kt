package com.example.jobfinder.UI.Statistical

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityStatisticalBinding
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.formatter.ValueFormatter

class StatisticalActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nút back về
        binding.backbtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        drawBarChart()

        // Hiển thị MonthYearPickerDialog và xử lí data BarChart
        binding.selectMonthYearBtn.setOnClickListener {
            val monthYearPickerDialog = MonthYearPickerDialog()
            monthYearPickerDialog.setListener { month, year ->
                val monthYearText = "Tháng $month/$year"
                binding.selectMonthYearBtn.text = monthYearText

                // Cập nhật biểu đồ với dữ liệu mới
                updateBarChartForMonthAndYear(month, year)
            }
            monthYearPickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }



    }
    private fun updateBarChartForMonthAndYear(month: Int, year: Int) {

    }

    private fun drawBarChart() {
        // Kiểu dữ liệu float (fill data vào thì chỉ đổi tham số Y thôi nhé)
        // CẤM ĐỘNG X nó lệch label với nhóm đấy
        val ColumnThuNhap = listOf(
            BarEntry(0.5f, 2000000f),
            BarEntry(1.5f, 204852f),
            BarEntry(2.5f, 643450f),
            BarEntry(3.5f, 54640f)
        )

        val ColumnChiTieu = listOf(
            BarEntry(0.5f, 436000f),
            BarEntry(1.5f, 23530f),
            BarEntry(2.5f, 154345f),
            BarEntry(3.5f, 5430f)
        )

        // Đổi màu cho các cột
        val thuNhapDataSet = BarDataSet(ColumnThuNhap, "Thu nhập").apply {
            color = ContextCompat.getColor(this@StatisticalActivity, R.color.income_color)
        }
        val chiTieuDataSet = BarDataSet(ColumnChiTieu, "Chi tiêu").apply {
            color = ContextCompat.getColor(this@StatisticalActivity, R.color.red)
        }

        // Gán data vào chart
        val data = BarData(thuNhapDataSet, chiTieuDataSet)
        setupAndApplyDataToChart(binding.barChart, data)

        // độ rộng của 1 cột trong 1 nhóm
        data.barWidth = 0.3f

        // Đổi tên label từng nhóm và config chart
        val weeks = arrayOf("Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4")
        configBarChart(weeks)
    }

    private fun configBarChart(weeks: Array<String>) {
        val groupSpace = 0.2f                              // Khoảng cách giữa các nhóm (tuần 1, tuần 2,...)
        val barSpace = 0.1f                                // Khoảng cách giữa các cột trong 1 nhóm (chi tiêu, thu nhập)
        val firstPosition = 0.5f                           // Vị trí đặt nhóm đầu tiên (tuần 1)

        binding.barChart.groupBars(firstPosition, groupSpace, barSpace)
        binding.barChart.description.isEnabled = false     // Không cần tiêu đề
        binding.barChart.setDrawGridBackground(true)       // Vẽ nền lưới sau cột
        binding.barChart.setPinchZoom(true)                // Cho phép zoom chart
        binding.barChart.animateY(1000)        // Độ trễ cho animation
        binding.barChart.axisRight.isEnabled = false       // Ẩn trục phải của chart

        // Chỉnh margin cho chú thích (legend)
        binding.barChart.setExtraOffsets(0f,0f,0f,15f)

        // Trục X
        binding.barChart.xAxis.apply {
            granularity = 1f                               // Khoảng cách giữa các điểm trên trục
            setDrawGridLines(false)                        // không kẻ lưới cho trục X
            textColor = getColor(R.color.black)
            position = XAxis.XAxisPosition.BOTTOM           // Vị trí đặt trục X
            axisMinimum = 0.5f                              // Giá trị tối thiểu trục X
            axisMaximum = weeks.size.toFloat() + 0.5f       // Giá trị tối đa trục X

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    // Chỉ định vị trí của các nhãn trục X
                    return if (value >= 0.5 && value < weeks.size + 0.5) weeks[(value - 0.5).toInt()] else ""
                }
            }
        }

        // Trục y
        binding.barChart.axisLeft.apply {
            setDrawGridLines(true)
            textColor = getColor(R.color.black)
        }
    }

    private fun setupValueFormatter(data: ChartData<*>) {
        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when {
                    value >= 1000000 -> "${"%.1f".format(value / 1000000)}M"
                    value >= 1000 -> "${"%.0f".format(value / 1000)}K"
                    else -> "%.0f".format(value)
                }
            }
        }

        for (set in data.dataSets) {
            set.valueFormatter = formatter
            set.valueTextColor = Color.BLACK
            set.valueTextSize = 10f
        }
    }

    private fun setupAndApplyDataToChart(chart: Chart<*>, data: ChartData<*>) {
        chart.data = data
        setupValueFormatter(data) // Đặt format value hiển thị ở mỗi cột trên biểu đồ
        chart.invalidate() // Cập nhật cài đặt ở trên và áp dụng để vẽ chart
    }

}