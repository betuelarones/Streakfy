package com.app.myapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.myapplication.data.local.dao.TaskDao
import com.app.myapplication.data.local.entities.Task

@Database(
    entities = [Task::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}