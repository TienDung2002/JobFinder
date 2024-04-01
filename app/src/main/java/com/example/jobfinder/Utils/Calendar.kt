package com.example.jobfinder.Utils

import android.app.DatePickerDialog
import com.google.android.material.textfield.TextInputEditText
import android.content.Context


object Calendar {
    fun setupDatePicker(context: Context, editText: TextInputEditText) {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )

        // Set the minimum date to the current date
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

}