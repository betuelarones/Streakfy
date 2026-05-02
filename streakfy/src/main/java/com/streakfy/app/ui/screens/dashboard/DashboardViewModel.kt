package com.streakfy.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.entities.StreakRecord
import com.streakfy.app.data.repository.FocusSessionRepository
import com.streakfy.app.data.repository.StreakRepository
import com.streakfy.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(

    private val taskRepo: TaskRepository,
    private val streakRepo: StreakRepository,
    private val focusRepo: FocusSessionRepository
) : ViewModel() {

    val tasks = taskRepo.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streak = streakRepo.getStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusTime: StateFlow<Int> = MutableStateFlow(0)
    val completedTasksToday: StateFlow<Int> = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            combine(taskRepo.getActiveTasks(), focusRepo.getSessionsFlow()) { tasks, sessions ->
                val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = todayFormat.format(Date())

                val todayTasksCompleted = tasks.count {
                    it.completed && it.completedAt != null &&
                    todayFormat.format(Date(it.completedAt)) == today
                }

                val todayFocusMinutes = sessions.filter {
                    it.completed && it.endTime != null &&
                    todayFormat.format(Date(it.endTime)) == today
                }.sumOf { it.duration }

                completedTasksToday as MutableStateFlow
                (completedTasksToday as MutableStateFlow).value = todayTasksCompleted
                totalFocusTime as MutableStateFlow
                (totalFocusTime as MutableStateFlow).value = todayFocusMinutes
            }.collect { }
        }
    }

    fun checkToday() {
        viewModelScope.launch {

            val todayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = todayFormat.format(Date())

            val allTasks = taskRepo.getTasksOnce()
            val allSessions = focusRepo.getSessionsOnce()

            val todayTasks = allTasks.filter {
                it.completed &&
                        it.completedAt != null &&
                        todayFormat.format(Date(it.completedAt)) == today
            }

            val todayFocus = allSessions.filter {
                it.completed &&
                        it.endTime != null &&
                        todayFormat.format(Date(it.endTime)) == today
            }

            val totalFocusMinutes = todayFocus.sumOf { it.duration }

            val meetsCriteria =
                todayTasks.isNotEmpty() || totalFocusMinutes >= 25

            val record = StreakRecord(
                date = today,
                completionPercentage = if (meetsCriteria) 100 else 0,
                focusMinutes = totalFocusMinutes,
                tasksCompleted = todayTasks.size
            )

            streakRepo.save(record)
        }
    }

    fun toggleTask(task: com.streakfy.app.data.local.entities.Task) {
        viewModelScope.launch {
            taskRepo.toggleTask(task)
        }
    }
}