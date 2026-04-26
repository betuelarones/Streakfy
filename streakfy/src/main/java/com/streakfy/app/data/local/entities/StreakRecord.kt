package com.streakfy.app.data.local.entities

import androidx.room.Entity

@Entity(tableName = "streak_records")
data class StreakRecord(
    val date: String,
    val completionPercentage: Int,
    val focusMinutes: Int,
    val tasksCompleted: Int
)