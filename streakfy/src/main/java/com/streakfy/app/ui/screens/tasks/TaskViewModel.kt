package com.streakfy.app.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.entities.Priority
import com.streakfy.app.data.local.entities.Task
import com.streakfy.app.data.local.entities.TaskTag
import com.streakfy.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    private val _selectedTag = MutableStateFlow<TaskTag?>(null)
    val selectedTag = _selectedTag
    val tasks = combine(
        repo.getTasks(),
        selectedTag
    ) { tasks, tag ->
        if (tag == null) tasks
        else tasks.filter { it.tag == tag }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

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


    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }

    fun setFilter(tag: TaskTag?) {
        _selectedTag.value = tag
    }
}
