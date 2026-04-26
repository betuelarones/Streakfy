package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val priority: Priority,
    val tag: TaskTag,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val order: Int = 0
)

enum class Priority {
    HIGH, MEDIUM, LOW
}

enum class TaskTag {
    WORK, PERSONAL, STUDY
}