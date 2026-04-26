package com.app.myapplication.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.myapplication.data.local.entities.Priority
import com.app.myapplication.data.local.entities.Task
import com.app.myapplication.data.local.entities.TaskTag
import com.app.myapplication.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {
    val tasks = repo.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String) {
        viewModelScope.launch {
            repo.insert(
                Task(
                    title = title,
                    priority = Priority.MEDIUM,
                    tag = TaskTag.PERSONAL
                )
            )
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repo.update(task.copy(completed = !task.completed))
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }
}
