package com.app.myapplication.data.repository

import com.app.myapplication.data.local.dao.TaskDao
import com.app.myapplication.data.local.entities.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    fun getTasks(): Flow<List<Task>> = dao.getTasks()

    suspend fun insert(task: Task) = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)
}