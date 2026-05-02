package com.streakfy.app.ui.screens.focus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streakfy.app.MainApplication
import com.streakfy.app.data.local.entities.FocusSession
import com.streakfy.app.data.repository.FocusSessionRepository
import kotlinx.coroutines.launch

class FocusViewModel(private val repo: FocusSessionRepository) : ViewModel() {

    var timeLeft by mutableStateOf(25 * 60) // 25 minutes in seconds
        private set

    var isRunning by mutableStateOf(false)
        private set

    var totalTime by mutableStateOf(25 * 60)
        private set

    private var timer: CountDownTimer? = null
    private var startTime: Long = 0

    fun start() {
        if (isRunning) return

        startTime = System.currentTimeMillis()
        isRunning = true

        timer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                isRunning = false
                timeLeft = 0
                onSessionComplete()
            }
        }.start()
    }

    fun pause() {
        timer?.cancel()
        isRunning = false
    }

    fun reset() {
        timer?.cancel()
        timeLeft = totalTime
        isRunning = false
    }

    fun setPreset(minutes: Int) {
        timer?.cancel()
        totalTime = minutes * 60
        timeLeft = totalTime
        isRunning = false
    }

    private fun onSessionComplete() {
        viewModelScope.launch {
            repo.insert(
                FocusSession(
                    duration = totalTime / 60,
                    startTime = startTime,
                    endTime = System.currentTimeMillis(),
                    completed = true
                )
            )
            showNotification()
        }
    }

    private fun showNotification() {
        val context = MainApplication.instance
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "focus_channel",
            "Focus Sessions",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = android.app.Notification.Builder(context, "focus_channel")
            .setContentTitle("¡Sesión Completada!")
            .setContentText("Has completado tu sesión de foco de ${totalTime / 60} minutos")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()

        notificationManager.notify(1, notification)
    }
}
