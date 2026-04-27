package com.streakfy.app.ui.screens.focus

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.streakfy.app.data.repository.FocusSessionRepository

class FocusViewModel(private val repo: FocusSessionRepository) : ViewModel(){

    var timeLeft by mutableStateOf(1500)
        private set

    var isRunning by mutableStateOf(false)
        private set

    private var timer: CountDownTimer? = null

    fun start() {
        timer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                isRunning = false
            }
        }.start()
    }

    fun pause() {
        timer?.cancel()
        isRunning = false
    }

    fun reset() {
        timer?.cancel()
        timeLeft = 1500
        isRunning = false
    }
}