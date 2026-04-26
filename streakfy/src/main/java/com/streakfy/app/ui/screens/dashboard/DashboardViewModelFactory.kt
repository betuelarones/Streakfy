package com.streakfy.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.repository.StreakRepository
import com.streakfy.app.data.repository.TaskRepository

class DashboardViewModelFactory(
    private val taskRepo: TaskRepository,
    private val streakRepo: StreakRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(taskRepo, streakRepo) as T
    }
}