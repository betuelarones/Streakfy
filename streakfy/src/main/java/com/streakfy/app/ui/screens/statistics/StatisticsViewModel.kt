package com.streakfy.app.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(private val db: AppDatabase) : ViewModel() {

    val totalFocusTime: StateFlow<Int> = MutableStateFlow(0)
    val completedTasks: StateFlow<Int> = MutableStateFlow(0)
    val productiveDays: StateFlow<Int> = MutableStateFlow(0)
    val productivityScore: StateFlow<Int> = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            val sessions = db.focusSessionDao().getCompletedSessions()
            (totalFocusTime as MutableStateFlow).value = sessions.sumOf { it.duration }

            val tasks = db.taskDao().getAllOnce()
            (completedTasks as MutableStateFlow).value = tasks.count { it.completed }

            val streaks = db.streakDao().getAllOnce()
            (productiveDays as MutableStateFlow).value = streaks.count { it.completionPercentage == 100 }

            val totalDays = 7
            val productive = productiveDays.value
            (productivityScore as MutableStateFlow).value = if (totalDays > 0) (productive * 100) / totalDays else 0
        }
    }
}
