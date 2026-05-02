package com.streakfy.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.database.AppDatabase
import com.streakfy.app.data.local.entities.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val db: AppDatabase) : ViewModel() {

    val profile: Flow<UserProfile?> = db.userProfileDao().getProfileFlow()

    val unlockedAchievements: Flow<List<com.streakfy.app.data.local.entities.Achievement>> =
        db.achievementDao().getUnlocked()

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            val current = db.userProfileDao().getProfile() ?: UserProfile(name = name, email = email)
            db.userProfileDao().insert(current.copy(name = name, email = email))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val current = db.userProfileDao().getProfile() ?: UserProfile(name = "Usuario", email = "usuario@streakfy.com")
            db.userProfileDao().insert(current.copy(notificationsEnabled = enabled))
        }
    }
}

fun com.streakfy.app.data.local.dao.UserProfileDao.getProfileFlow(): kotlinx.coroutines.flow.Flow<com.streakfy.app.data.local.entities.UserProfile?> {
    return kotlinx.coroutines.flow.flow {
        emit(getProfile())
    }
}
