package com.streakfy.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.streakfy.app.data.local.entities.FocusSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert
    suspend fun insert(session: FocusSession)

    @Update
    suspend fun update(session: FocusSession)

    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    fun getAll(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    fun getByDate(date: Long): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE completed = 1")
    suspend fun getCompletedSessions(): List<FocusSession>

    @Query("SELECT SUM(duration) FROM focus_sessions WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') AND completed = 1")
    suspend fun getTotalFocusTimeForDate(date: Long): Int?

    @Query("SELECT * FROM focus_sessions")
    suspend fun getAllOnce(): List<FocusSession>
}