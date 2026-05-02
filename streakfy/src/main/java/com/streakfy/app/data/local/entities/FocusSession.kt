package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val duration: Int,
    val startTime: Long,
    val endTime: Long? = null,
    val completed: Boolean = false,
    val pausedTime: Long = 0,
    val taskId: Long? = null
)