package com.streakfy.app.data.repository

import com.streakfy.app.data.local.dao.FocusSessionDao
import com.streakfy.app.data.local.entities.FocusSession
import kotlinx.coroutines.flow.Flow

class FocusSessionRepository(private val dao: FocusSessionDao) {

    fun getSessions() = dao.getAll()

    fun getSessionsFlow(): Flow<List<FocusSession>> = dao.getAll()

    suspend fun insert(session: FocusSession) {
        dao.insert(session)
    }

    suspend fun update(session: FocusSession) {
        dao.update(session)
    }

    suspend fun getSessionsOnce(): List<FocusSession> {
        return dao.getAllOnce()
    }
}