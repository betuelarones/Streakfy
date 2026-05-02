package com.streakfy.app.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.streakfy.app.data.local.entities.Achievement
import com.streakfy.app.data.local.entities.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `calendar_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `type` TEXT NOT NULL, `createdAt` INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP)")
            database.execSQL("CREATE TABLE IF NOT EXISTS `achievements` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `icon` TEXT NOT NULL, `unlocked` INTEGER NOT NULL DEFAULT 0, `unlockedAt` INTEGER, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `avatarUri` TEXT, `xp` INTEGER NOT NULL DEFAULT 0, `level` INTEGER NOT NULL DEFAULT 1, `notificationsEnabled` INTEGER NOT NULL DEFAULT 1, `calendarSyncEnabled` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))")
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "streakfy_db"
            ).addMigrations(MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getDatabase(context)
                            initializeAchievements(database.achievementDao())
                            initializeUserProfile(database.userProfileDao())
                        }
                    }
                })
                .build()
            INSTANCE = instance
            instance
        }
    }

    private suspend fun initializeAchievements(dao: com.streakfy.app.data.local.dao.AchievementDao) {
        val achievements = listOf(
            Achievement("streak_7", "Racha de 7 días", "Completa 7 días consecutivos", "🔥", false),
            Achievement("streak_30", "Racha de 30 días", "Completa 30 días consecutivos", "🏆", false),
            Achievement("tasks_100", "100 Tareas", "Completa 100 tareas", "✅", false),
            Achievement("focus_10h", "10 Horas de Foco", "Acumula 10 horas en modo foco", "⏱️", false),
            Achievement("early_bird", "Madrugador", "Completa tareas antes de las 8 AM", "🌅", false),
            Achievement("night_owl", "Nocturno", "Completa tareas después de las 10 PM", "🦉", false)
        )
        achievements.forEach { dao.insert(it) }
    }

    private suspend fun initializeUserProfile(dao: com.streakfy.app.data.local.dao.UserProfileDao) {
        dao.insert(UserProfile(name = "Usuario", email = "usuario@streakfy.com"))
    }
}
