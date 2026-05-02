package com.streakfy.app.ui.screens.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streakfy.app.data.repository.FocusSessionRepository

class FocusViewModelFactory(
    private val repo: FocusSessionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FocusViewModel(repo) as T
    }
}
