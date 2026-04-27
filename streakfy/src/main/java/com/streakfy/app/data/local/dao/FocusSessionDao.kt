package com.streakfy.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streakfy.app.data.local.entities.FocusSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert
    suspend fun insert(session: FocusSession)

    @Query("SELECT * FROM `focus-sessions` ORDER BY startTime DESC")
    fun getAll(): Flow<List<FocusSession>>

    @Query("SELECT * FROM `focus-sessions` ")
    suspend fun getAllOnce(): List<FocusSession>

}