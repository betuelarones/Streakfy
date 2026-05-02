package com.streakfy.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val email: String,
    val avatarUri: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val notificationsEnabled: Boolean = true,
    val calendarSyncEnabled: Boolean = false
)
