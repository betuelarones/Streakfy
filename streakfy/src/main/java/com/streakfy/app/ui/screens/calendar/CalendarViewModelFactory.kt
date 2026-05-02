package com.streakfy.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.local.dao.CalendarEventDao

class CalendarViewModelFactory(
    private val dao: CalendarEventDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(dao) as T
    }
}
