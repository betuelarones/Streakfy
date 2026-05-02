package com.streakfy.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.dao.CalendarEventDao
import com.streakfy.app.data.local.entities.CalendarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalendarViewModel(private val dao: CalendarEventDao) : ViewModel() {

    fun getEventsForDate(date: Long): Flow<List<CalendarEvent>> {
        return dao.getByDate(date)
    }

    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            dao.insert(event)
        }
    }

    fun deleteEvent(event: CalendarEvent) {
        viewModelScope.launch {
            dao.delete(event)
        }
    }
}
