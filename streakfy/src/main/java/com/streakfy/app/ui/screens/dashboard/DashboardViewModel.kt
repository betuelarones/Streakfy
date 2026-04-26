package com.streakfy.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.entities.StreakRecord
import com.streakfy.app.data.repository.StreakRepository
import com.streakfy.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(

    private val taskRepo: TaskRepository,
    private val streakRepo: StreakRepository
) : ViewModel() {

    val tasks = taskRepo.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streak = streakRepo.getStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun checkToday() {
        viewModelScope.launch {
            val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = todayFormat.format(Date())

            val todayTasks = tasks.value.filter {
                it.completed && it.completedAt != null &&
                        todayFormat.format(Date(it.completedAt)) == today
            }

            val record = StreakRecord(
                date = today,
                completionPercentage = if (todayTasks.isNotEmpty()) 100 else 0,
                focusMinutes = 0,
                tasksCompleted = todayTasks.size
            )

            streakRepo.save(record)
        }
    }
}