package com.streakfy.app.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.repository.TaskRepository

class TaskViewModelFactory (
    private val repo: TaskRepository
) : ViewModelProvider.Factory {

    override fun  <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(repo) as T
    }
}