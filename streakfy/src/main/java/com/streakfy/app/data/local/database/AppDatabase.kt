package com.streakfy.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.streakfy.app.data.local.dao.AchievementDao
import com.streakfy.app.data.local.dao.CalendarEventDao
import com.streakfy.app.data.local.dao.FocusSessionDao
import com.streakfy.app.data.local.dao.StreakDao
import com.streakfy.app.data.local.dao.TaskDao
import com.streakfy.app.data.local.dao.UserProfileDao
import com.streakfy.app.data.local.entities.Achievement
import com.streakfy.app.data.local.entities.CalendarEvent
import com.streakfy.app.data.local.entities.FocusSession
import com.streakfy.app.data.local.entities.StreakRecord
import com.streakfy.app.data.local.entities.Task
import com.streakfy.app.data.local.entities.UserProfile

@Database(
    entities = [
        Task::class,
        StreakRecord::class,
        FocusSession::class,
        CalendarEvent::class,
        Achievement::class,
        UserProfile::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun streakDao(): StreakDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userProfileDao(): UserProfileDao
}