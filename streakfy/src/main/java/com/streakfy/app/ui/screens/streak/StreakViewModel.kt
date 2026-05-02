package com.streakfy.app.ui.screens.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.data.local.database.AppDatabase
import com.streakfy.app.data.local.entities.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StreakViewModel(private val db: AppDatabase) : ViewModel() {

    val currentStreak: StateFlow<Int> = MutableStateFlow(0)
    val longestStreak: StateFlow<com.streakfy.app.data.local.entities.StreakRecord?> = MutableStateFlow(null)
    val weeklyProgress: StateFlow<List<Pair<String, Int>>> = MutableStateFlow(emptyList())
    val achievements: StateFlow<List<Achievement>> = MutableStateFlow(emptyList())
    val freezesLeft: StateFlow<Int> = MutableStateFlow(2)

    init {
        viewModelScope.launch {
            val streakDao = db.streakDao()
            val achievementDao = db.achievementDao()

            val streaks = streakDao.getAllOnce()
            (currentStreak as MutableStateFlow).value = streaks.count { it.completionPercentage == 100 }

            (longestStreak as MutableStateFlow).value = streakDao.getLongestStreak()

            val calendar = Calendar.getInstance()
            val weekData = mutableListOf<Pair<String, Int>>()
            for (i in 6 downTo 0) {
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val dateStr = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                val dayName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "L"
                    Calendar.TUESDAY -> "M"
                    Calendar.WEDNESDAY -> "X"
                    Calendar.THURSDAY -> "J"
                    Calendar.FRIDAY -> "V"
                    Calendar.SATURDAY -> "S"
                    Calendar.SUNDAY -> "D"
                    else -> ""
                }
                val record = streaks.find { it.date == dateStr }
                weekData.add(dayName to (record?.completionPercentage ?: 0))
            }
            (weeklyProgress as MutableStateFlow).value = weekData

            (achievements as MutableStateFlow).value = achievementDao.getAllOnce()
        }
    }
}
