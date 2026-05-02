package com.streakfy.app.ui.screens.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.local.database.AppDatabase

class StreakViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StreakViewModel(db) as T
    }
}
