package com.streakfy.app.data.local.database

import androidx.room.Room
import android.content.Context

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "streakfy_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}