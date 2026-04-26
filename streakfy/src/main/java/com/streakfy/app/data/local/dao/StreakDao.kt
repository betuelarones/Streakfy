package com.streakfy.app.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streakfy.app.data.local.entities.StreakRecord
import kotlinx.coroutines.flow.Flow

interface StreakDao {
    @Query("SELECT * FROM streak_records ORDER BY date DESC")
    fun getAll(): Flow<List<StreakRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: StreakRecord)
}