package com.streakfy.app.data.local.dao

import androidx.room.*
import com.streakfy.app.data.local.entities.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun getAll(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements")
    suspend fun getAllOnce(): List<Achievement>

    @Query("SELECT * FROM achievements WHERE unlocked = 1")
    fun getUnlocked(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getById(id: String): Achievement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: Achievement)

    @Update
    suspend fun update(achievement: Achievement)
}
