package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTime: Long,
    val endTime: Long,
    val type: EventType,
    val createdAt: Long = System.currentTimeMillis()
)

enum class EventType {
    WORK, PERSONAL, FOCUS
}
