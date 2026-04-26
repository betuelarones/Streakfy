package com.streakfy.app.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streakfy.app.data.local.entities.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    tasks: List<Task>,
    onGoToTasks: () -> Unit
) {
    val today = SimpleDateFormat("EEEE, dd MMM", Locale("es", "ES"))
        .format(Date())


    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 6..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = greeting, style = MaterialTheme.typography.headlineSmall)
        Text(text = today, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tareas de hoy")

        val todayTasks = tasks.take(3)

        LazyColumn {
            items(todayTasks) { task ->
                Text("- ${task.title}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGoToTasks) {
            Text("Ver todas")
        }
    }
}