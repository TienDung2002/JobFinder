package com.example.jobfinder.UI.Statistical

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.jobfinder.R
import java.util.Calendar

class MonthYearPickerDialog : DialogFragment() {

    private var listener: ((Int, Int) -> Unit)? = null

    fun setListener(listener: (Int, Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_month_year_picker)
        dialog.setTitle("Chọn tháng và năm")

        val monthPicker = dialog.findViewById<NumberPicker>(R.id.picker_month)
        val yearPicker = dialog.findViewById<NumberPicker>(R.id.picker_year)

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = Calendar.getInstance().get(Calendar.MONTH) + 1

        val year = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = year - 50
        yearPicker.maxValue = year + 50
        yearPicker.value = year

        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            listener?.invoke(monthPicker.value, yearPicker.value)
            dismiss()
        }

        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }

        return dialog
    }
}
