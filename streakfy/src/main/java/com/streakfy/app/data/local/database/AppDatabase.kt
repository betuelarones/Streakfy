package com.streakfy.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.streakfy.app.data.local.dao.FocusSessionDao
import com.streakfy.app.data.local.dao.StreakDao
import com.streakfy.app.data.local.dao.TaskDao
import com.streakfy.app.data.local.entities.FocusSession
import com.streakfy.app.data.local.entities.StreakRecord
import com.streakfy.app.data.local.entities.Task

@Database(
    entities = [
        Task::class,
        StreakRecord::class,
        FocusSession::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun streakDao(): StreakDao
    abstract fun focusSessionDao(): FocusSessionDao
}