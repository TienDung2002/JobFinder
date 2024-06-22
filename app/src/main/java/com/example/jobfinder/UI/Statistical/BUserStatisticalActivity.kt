package com.example.jobfinder.UI.Statistical

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.Utils.IncomeHandle
import com.example.jobfinder.databinding.ActivityStatisticalBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class BUserStatisticalActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticalBinding
    private val viewModel: IncomeViewModel by viewModels()
    private val uid = GetData.getCurrentUserId()
    private val today = GetData.getCurrentDateTime()

    private var selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var selectedYear = Calendar.getInstance().get(Calendar.YEAR)

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

        drawChartBuser()

        // Dialog Barchart
        binding.selectMonthYearBtn.setOnClickListener {
            val monthYearPickerDialog = MonthYearPickerDialog(selectedMonth, selectedYear)
            monthYearPickerDialog.setListener { month, year ->
                selectedMonth = month
                selectedYear = year
                val monthYearText = "Tháng $month /$year"
                binding.selectMonthYearBtn.text = monthYearText
                updateBarCharMonYea(month, year)
            }
            monthYearPickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        // Dialog Line chart
        binding.BuserselectMonthYearBtn.setOnClickListener {
            val monthYearPickerDialog = MonthYearPickerDialog(selectedMonth, selectedYear)
            monthYearPickerDialog.setListener { month, year ->
                selectedMonth = month
                selectedYear = year
                val monthYearText = "Tháng $month /$year"
                binding.BuserselectMonthYearBtn.text = monthYearText
                updateLineChartBuser(month, year)
            }
            monthYearPickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

    }
    private fun updateBarCharMonYea(month: Int, year: Int) {
        val incomeList = viewModel.incomeList.value
        incomeList?.let {
            val weeklyTotals = IncomeHandle.calculateWeeklyIncome(incomeList, year, month)
            drawBarChart(weeklyTotals, mapOf())
//            weeklyTotals.forEach { (weekNumber, total) ->
//                Log.d("dkjbfkjds","Tuần $weekNumber: Tổng thu nhập = $total")
//            }
        }
    }


    private fun updateLineChartBuser(month: Int, year: Int) {

    }

    private fun drawBarChart(weekAndTotalIncome: Map<Int,Double>, weekAndTotalExpense: Map<Int, Double>) {
        // Kiểu dữ liệu float (fill data vào thì chỉ đổi tham số Y thôi nhé)
        // CẤM ĐỘNG X nó lệch label với nhóm đấy
        val mutableColumnThuNhap = mutableListOf<BarEntry>()

        val mutableColumnChiTieu = mutableListOf<BarEntry>()

        var positionX = 0.5f

        // Duyệt qua weekAndTotal và thêm BarEntry vào mutableColumnThuNhap
        weekAndTotalIncome.forEach { (_, total) ->
            mutableColumnThuNhap.add(BarEntry(positionX, total.toFloat()))
            positionX += 1.0f // Tăng vị trí x thêm 1.0 sau mỗi tuần
        }

        // Chuyển đổi MutableList thành List
        val ColumnThuNhap: List<BarEntry> = mutableColumnThuNhap.toList()

        weekAndTotalExpense.forEach { (_, total) ->
            mutableColumnChiTieu.add(BarEntry(positionX, total.toFloat()))
            positionX += 1.0f // Tăng vị trí x thêm 1.0 sau mỗi tuần
        }

        // Chuyển đổi MutableList thành List
        val ColumnChiTieu: List<BarEntry> = mutableColumnThuNhap.toList()


//        val ColumnThuNhap = listOf(
//            BarEntry(0.5f, 2000000f),
//            BarEntry(1.5f, 204852f),
//            BarEntry(2.5f, 643450f),
//            BarEntry(3.5f, 54640f)
//        )

//        val ColumnChiTieu = listOf(
//            BarEntry(0.5f, 436000f),
//            BarEntry(1.5f, 23530f),
//            BarEntry(2.5f, 154345f),
//            BarEntry(3.5f, 5430f)
//        )

//        val weeks = arrayOf("Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4")
        val weeks = weekAndTotalIncome.keys.map { "Tuần $it" }.toTypedArray()

        // Không có data thì đây là giá trị mặc định
        val defaultValues = weeks.indices.map { BarEntry(it + 0.5f, 0f) }

        // Gán giá trị default và đổi màu cột
        val thuNhapDataSet = BarDataSet(ColumnThuNhap.ifEmpty { defaultValues }, getString(R.string.Sta_labelBarchart_income)).apply {
            color = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.income_color)
        }
        val chiTieuDataSet = BarDataSet(ColumnChiTieu.ifEmpty { defaultValues }, getString(R.string.Sta_labelBarchart_expenditure)).apply {
            color = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.red)
        }

        val data = BarData(thuNhapDataSet, chiTieuDataSet)
        SetupAndApplyDataToBarChart(binding.barChart, data, weeks)
    }

    private fun SetupAndApplyDataToBarChart(chart: BarChart, data: ChartData<*>, weeks: Array<String>) {
        chart.apply {
            this.data = data as BarData?

            setupValueFormatter(data, getColor(R.color.black), 10f) // Đặt format value hiển thị ở mỗi cột trên biểu đồ
            val groupSpace = 0.2f                              // Khoảng cách giữa các nhóm (tuần 1, tuần 2,...)
            val barSpace = 0.1f                                // Khoảng cách giữa các cột trong 1 nhóm (chi tiêu, thu nhập)
            val firstPosition = 0.5f                           // Vị trí đặt nhóm đầu tiên (tuần 1)
            data.barWidth = 0.3f                               // độ rộng của 1 cột trong 1 nhóm

            groupBars(firstPosition, groupSpace, barSpace)
            description.isEnabled = false     // Không cần tiêu đề
            setDrawGridBackground(true)       // Vẽ nền lưới sau cột
            setPinchZoom(true)                // Cho phép zoom chart
            animateY(1000)        // Độ trễ cho animation
            axisRight.isEnabled = false       // Ẩn trục phải của chart

            // Chú thích
            legend.apply {
                setExtraOffsets(0f,0f,0f,15f)
                form = Legend.LegendForm.LINE
                xEntrySpace = 30f       // Khoảng cách của legend trục X
                textSize = 14f
            }
            // Trục X
            xAxis.apply {
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
            // Trục Y
            axisLeft.apply {
                setDrawGridLines(true)
                textColor = getColor(R.color.black)
            }

            invalidate() // Cập nhật cài đặt ở trên và áp dụng để vẽ chart
        }
    }



    private fun drawPieChart() {
        // Chỉ cần sửa tham số 1 của PieEntry()
        val pieEntries = listOf(
            PieEntry(3205230f, getString(R.string.Sta_wallet)),
            PieEntry(5040400f, getString(R.string.Sta_card))
        )

        // Màu của các trường
        val pieDataSet = PieDataSet(pieEntries, "").apply {
            colors = listOf(
                ContextCompat.getColor(this@BUserStatisticalActivity, R.color.primary_color2),
                ContextCompat.getColor(this@BUserStatisticalActivity, R.color.blue)
            )
        }

        val pieData = PieData(pieDataSet)

        setupAndApplyDataToPieChart(binding.pieChart, pieData)
    }

    private fun setupAndApplyDataToPieChart(chart: PieChart, data: PieData) {
        chart.apply {
            this.data = data
            description.isEnabled = false                       // Ẩn mô tả của biểu đồ
            isDrawHoleEnabled = true                            // Cho phép vẽ lỗ ở giữa biểu đồ
            holeRadius = 40f                                    // Đặt bán kính của lỗ ở giữa
            setEntryLabelColor(getColor(R.color.white))         // Đặt màu sắc cho nhãn của các phần
            animateY(1000)                          // Thêm animation khi hiển thị biểu đồ

            // Chỉnh chú thích (legend)
            legend.apply {
                isEnabled = true                                                // Hiển thị chú thích
                textColor = getColor(R.color.black)
                textSize = 15f
                setXEntrySpace(15f)                                             // Khoảng cách giữa các legend, trục X
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER       // Căn chú thích theo chiều dọc
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER   // Căn chú thích theo chiều ngang
                form = Legend.LegendForm.CIRCLE                                 // Đổi hình dạng của chú thích thành chấm tròn
            }

            setupValueFormatter(data, getColor(R.color.white), 14f) // Verify định dạng value
            invalidate()                                        // Cập nhật cài đặt ở trên và áp dụng để vẽ chart
        }
        // chỉnh cỡ chữ và màu của data
        data.apply {
            setValueTextSize(14f)
            setValueTextColor(Color.WHITE)
        }
    }



    private fun drawLineChart(label: String, chart: LineChart) {
        // Dữ liệu mẫu cho LineChart
        val lineEntries = listOf(
            Entry(1f, 160f),
            Entry(2f, 150f),
            Entry(3f, 172f),
            Entry(4f, 120f),
            Entry(5f, 80f),
            Entry(6f, 20f),
            Entry(7f, 200f),
            Entry(8f, 430f),
            Entry(9f, 250f),
            Entry(10f, 360f),
            Entry(11f, 23f),
            Entry(12f, 9f)
        )

        val lineDataSet = LineDataSet(lineEntries, label).apply {
            color = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.primary_color2)
            valueTextColor = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.black)
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.filter_color)
        }

        val data = LineData(lineDataSet)
        setupAndApplyDataToLineChart(chart, data)
    }

    private fun setupAndApplyDataToLineChart(chart: LineChart, data: LineData) {
        chart.apply {
            this.data = data
            description.isEnabled = false
            setDrawGridBackground(true)
            setPinchZoom(true)
            animateY(1000)
            axisRight.isEnabled = false

            // Trục x
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                labelCount = 12
                textColor = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.black)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "T${value.toInt()}"
                    }
                }
            }
            // Trục y
            axisLeft.apply {
                textColor = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.black)
                setDrawGridLines(true)
            }
            // Chú thích
            legend.apply {
                setExtraOffsets(0f,0f,0f,15f)
                xEntrySpace = 30f
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM       // Căn chiều dọc
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT    // Căn chiều ngang
                textColor = ContextCompat.getColor(this@BUserStatisticalActivity, R.color.black)
                textSize = 14f
            }

            invalidate()
        }
    }



    private fun setupValueFormatter(data: ChartData<*>, valueTextColor: Int, valueTextSize: Float) {
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
            set.valueTextColor = valueTextColor
            set.valueTextSize = valueTextSize
        }
    }



//    @SuppressLint("NewApi")
//    private fun drawChartBuser() {
//        val lineChartTitle = getString(R.string.Sta_workingHourPMonth)
//        val legend = getString(R.string.Sta_workedHourPMonth_legend)
//        val lineChart = binding.NuserlineChart
//
//        val todayString = GetData.getDateFromString(today)
//
//        val todayDate = LocalDate.parse(todayString, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//
//        binding.NuserLineChartWrap.visibility = View.VISIBLE
//        binding.BuserlineChartTitle.text = lineChartTitle
//
//        viewModel.fetchIncome(uid.toString())
//
//        viewModel.incomeList.observe(this){ newIncomeList->
//            val weeklyTotals = IncomeHandle.calculateWeeklyIncome(newIncomeList, todayDate.year, todayDate.monthValue)
//            drawBarChart(weeklyTotals, mapOf())
//            weeklyTotals.forEach { (weekNumber, total) ->
//                Log.d("dkjbfkjds","Tuần $weekNumber: Tổng thu nhập = $total")
//            }
//        }
//
//        drawPieChart()
//        drawLineChart(legend, lineChart)
//    }

    private fun drawChartBuser() {
        val lineChartTitle = getString(R.string.Sta_jobs_posted)
        val legend = getString(R.string.Sta_jobs_posted_legend)
        val lineChart = binding.BuserlineChart

        binding.BuserLineChartWrap.visibility = View.VISIBLE
        binding.BuserlineChartTitle.text = lineChartTitle

//        drawBarChart()
        drawPieChart()
        drawLineChart(legend, lineChart)
    }

}