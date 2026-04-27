package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streak_records")
data class StreakRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val completionPercentage: Int,
    val focusMinutes: Int,
    val tasksCompleted: Int
)