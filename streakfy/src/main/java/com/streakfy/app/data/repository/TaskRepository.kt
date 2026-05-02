package com.streakfy.app.data.repository

import com.streakfy.app.data.local.dao.TaskDao
import com.streakfy.app.data.local.entities.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val dao: TaskDao) {

    fun getTasks(): Flow<List<Task>> = dao.getAllTasks()

    fun getActiveTasks(): Flow<List<Task>> = dao.getActiveTasks()

    suspend fun getTasksOnce(): List<Task> {
        val list = mutableListOf<Task>()
        dao.getAllTasks().collect { list.addAll(it) }
        return list
    }

    suspend fun insert(task: Task) = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun toggleTask(task: Task) {
        dao.update(task.copy(completed = !task.completed, completedAt = if (!task.completed) System.currentTimeMillis() else null))
    }
}