package com.streakfy.app.data.local.dao

import androidx.room.*
import com.streakfy.app.data.local.entities.Task
import com.streakfy.app.data.local.entities.TaskTag
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY `order` ASC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE tag = :tag ORDER BY `order` ASC")
    fun getTasksByTag(tag: TaskTag): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY `order` ASC")
    fun getTasksByDate(date: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY `order` ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    suspend fun getAllOnce(): List<Task>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("UPDATE tasks SET `order` = :order WHERE id = :taskId")
    suspend fun updateOrder(taskId: Long, order: Int)
}