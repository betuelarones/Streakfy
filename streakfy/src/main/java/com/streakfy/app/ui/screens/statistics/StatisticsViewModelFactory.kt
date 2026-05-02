package com.streakfy.app.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.local.database.AppDatabase

class StatisticsViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(db) as T
    }
}
