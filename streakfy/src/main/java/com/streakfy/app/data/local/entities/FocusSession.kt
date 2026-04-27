package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus-sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val duration: Int,
    val startTime: Long,
    val ednTime: Long? = null,
    val completed: Boolean = false  ,
    val pausedTime: Long = 0
)