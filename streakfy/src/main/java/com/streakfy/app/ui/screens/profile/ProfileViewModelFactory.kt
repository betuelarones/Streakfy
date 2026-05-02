package com.streakfy.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.local.database.AppDatabase

class ProfileViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(db) as T
    }
}
