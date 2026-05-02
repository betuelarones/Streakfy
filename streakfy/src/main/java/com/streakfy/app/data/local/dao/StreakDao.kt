package com.streakfy.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streakfy.app.data.local.entities.StreakRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak_records ORDER BY date DESC")
    fun getAll(): Flow<List<StreakRecord>>

    @Query("SELECT * FROM streak_records")
    suspend fun getAllOnce(): List<StreakRecord>

    @Query("SELECT * FROM streak_records WHERE date = :date")
    suspend fun getByDate(date: String): StreakRecord?

    @Query("SELECT * FROM streak_records ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): StreakRecord?

    @Query("SELECT COUNT(*) FROM streak_records WHERE completionPercentage = 100")
    suspend fun getStreakCount(): Int

    @Query("SELECT * FROM streak_records ORDER BY completionPercentage DESC LIMIT 1")
    suspend fun getLongestStreak(): StreakRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: StreakRecord)
}