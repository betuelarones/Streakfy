package com.streakfy.app.data.repository

import com.streakfy.app.data.local.dao.StreakDao
import com.streakfy.app.data.local.entities.StreakRecord
import kotlinx.coroutines.flow.Flow

class StreakRepository(private val dao: StreakDao) {

    fun getStreak(): Flow<List<StreakRecord>> = dao.getAll()

    suspend fun save(record: StreakRecord) {
        dao.insert(record)
    }
}