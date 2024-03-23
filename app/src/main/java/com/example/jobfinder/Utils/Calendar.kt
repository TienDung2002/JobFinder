package com.example.jobfinder.Utils

import android.app.DatePickerDialog
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair
import com.google.android.material.textfield.TextInputEditText
import android.content.Context

data class CalendarToggleState(var isCalendarVisible: Boolean)

object Calendar {
    fun showDatePickerDialog(
        fragmentManager: FragmentManager,
        calendarState: CalendarToggleState,
        title: String,
        showDateEditText: TextView
    ) {
        if (!calendarState.isCalendarVisible) {
            calendarState.isCalendarVisible = true

            // Giới hạn, không cho chọn ngày sinh tương lai
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setEnd(System.currentTimeMillis())

//            val builder = MaterialDatePicker.Builder.datePicker()
//                .setTitleText(title)
//                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
//                .setCalendarConstraints(constraintsBuilder.build())
//                .build()

            val builder = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(title)
                .setSelection(Pair(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            builder.addOnPositiveButtonClickListener { selection ->
                val selectedDateInMillis = selection as Long
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(selection))
                showDateEditText.setText(formattedDate)
                calendarState.isCalendarVisible = false
            }

            builder.addOnDismissListener { calendarState.isCalendarVisible = false }
            builder.addOnCancelListener { calendarState.isCalendarVisible = false }
            builder.show(fragmentManager, builder.toString())
        }
    }

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