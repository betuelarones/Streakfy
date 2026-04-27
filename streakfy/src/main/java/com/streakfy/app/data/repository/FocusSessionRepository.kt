package com.streakfy.app.data.repository

import com.streakfy.app.data.local.dao.FocusSessionDao
import com.streakfy.app.data.local.entities.FocusSession

class FocusSessionRepository(private val dao: FocusSessionDao) {

    fun getSessions() = dao.getAll()

    suspend fun insert(session: FocusSession) {
        dao.insert(session)
    }

    suspend fun getSessionsOnce(): List<FocusSession> {
        return dao.getAllOnce()
    }
}