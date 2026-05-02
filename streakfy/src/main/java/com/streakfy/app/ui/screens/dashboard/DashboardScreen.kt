package com.streakfy.app.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.data.repository.FocusSessionRepository
import com.streakfy.app.data.repository.StreakRepository
import com.streakfy.app.data.repository.TaskRepository
import com.streakfy.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable

@Composable
fun DashboardScreen(
    onGoToTasks: () -> Unit,
    onStartFocus: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val taskRepo = remember { TaskRepository(db.taskDao()) }
    val streakRepo = remember { StreakRepository(db.streakDao()) }
    val focusRepo = remember { FocusSessionRepository(db.focusSessionDao()) }

    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(taskRepo, streakRepo, focusRepo)
    )

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val streak by viewModel.streak.collectAsState(initial = emptyList())
    val totalFocusTime by viewModel.totalFocusTime.collectAsState(initial = 0)
    val completedTasks by viewModel.completedTasksToday.collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        viewModel.checkToday()
    }

    val today = SimpleDateFormat("EEEE, dd MMM", Locale("es", "ES")).format(Date())
    val streakDays = streak.size
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 6..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text(greeting, style = MaterialTheme.typography.headlineSmall, color = Foreground)
        Text(today.replaceFirstChar { it.uppercase() }, color = Muted, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Streak Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🔥", fontSize = 32.sp)
                Text("$streakDays", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Orange500)
                Text("Días consecutivos", color = Muted, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Stats Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(title = "Foco", value = "${totalFocusTime}m", modifier = Modifier.weight(1f), color = Blue600)
            StatCard(title = "Tareas", value = "$completedTasks", modifier = Modifier.weight(1f), color = Green400)
            StatCard(title = "Meta", value = "80%", modifier = Modifier.weight(1f), color = Purple600)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Tasks
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tareas de hoy", color = Foreground, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            TextButton(onClick = onGoToTasks) { Text("Ver Todo", color = Purple600) }
        }

        val todayTasks = tasks.take(3)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(todayTasks) { task ->
                TaskDashboardItem(task = task, onToggle = { viewModel.toggleTask(task) })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Quick Actions
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onStartFocus,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("▶ Foco", color = Color.White)
            }
            Button(
                onClick = onGoToTasks,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Purple600),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Tarea", color = Color.White)
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(title, color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
fun TaskDashboardItem(task: com.streakfy.app.data.local.entities.Task, onToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Green400)
            )
            Text(task.title, color = Foreground, modifier = Modifier.weight(1f))
        }
    }
}