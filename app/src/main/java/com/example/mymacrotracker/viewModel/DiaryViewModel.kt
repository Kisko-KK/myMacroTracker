package com.example.mymacrotracker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DiaryViewModel : ViewModel() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private var offset = 0

    init {
        updateCurrentDate()
    }

    fun updateCurrentDate() {
        _currentDate.value = getCurrentDateText()
    }

    fun moveBack() {
        offset--
        updateCurrentDate()
    }

    fun moveForward() {
        offset++
        updateCurrentDate()
    }

    private fun getCurrentDateText(): String {
        val currentDate = Calendar.getInstance().time
        val selectedDate = getSelectedDate()
        val diff = (currentDate.time - selectedDate.time) / (1000 * 60 * 60 * 24)

        return when (diff) {
            0L -> "Today"
            1L -> "Yesterday"
            -1L -> "Tomorrow"
            else -> dateFormat.format(selectedDate)
        }
    }
    public fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, offset)
        return dateFormat.format(calendar.time)
    }

    private fun getSelectedDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, offset)
        return calendar.time
    }
}
