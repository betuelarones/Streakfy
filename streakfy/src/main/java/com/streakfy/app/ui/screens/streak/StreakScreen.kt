package com.streakfy.app.ui.screens.streak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.ui.theme.*

@Composable
fun StreakScreen(
    viewModel: StreakViewModel = viewModel(
        factory = StreakViewModelFactory(
            DatabaseProvider.getDatabase(LocalContext.current)
        )
    )
) {
    val currentStreak by viewModel.currentStreak.collectAsState(initial = 0)
    val longestStreak by viewModel.longestStreak.collectAsState(initial = null)
    val weeklyProgress by viewModel.weeklyProgress.collectAsState(initial = emptyList())
    val achievements by viewModel.achievements.collectAsState(initial = emptyList())
    val freezesLeft by viewModel.freezesLeft.collectAsState(initial = 2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Sistema de Racha", style = MaterialTheme.typography.headlineMedium, color = Foreground)

        Spacer(modifier = Modifier.height(24.dp))

        // Current Streak with Fire Animation Placeholder
        Card(
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🔥", fontSize = 48.sp)
                Text("$currentStreak", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Orange500)
                Text("Días consecutivos", color = Muted)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Longest Streak
        Card(
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Racha más larga", color = Foreground)
                Text("${longestStreak?.completionPercentage ?: 0} días", color = Purple600, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weekly Progress Bar Chart
        Text("Progreso Semanal", color = Foreground, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyProgress.forEach { (day, percentage) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((percentage * 1.5).dp)
                            .background(if (percentage > 0) Orange500 else Border, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(day, color = Muted, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Freezes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Congelamientos disponibles", color = Foreground)
            Row {
                repeat(2) { index ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(if (index < freezesLeft) Blue600 else Border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("❄", fontSize = 16.sp)
                    }
                    if (index == 0) Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Achievements
        Text("Logros", color = Foreground, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            achievements.take(4).forEach { achievement ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Card),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(achievement.icon, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(achievement.title, color = Foreground, fontWeight = FontWeight.Medium)
                            Text(achievement.description, color = Muted, fontSize = 12.sp)
                        }
                        if (achievement.unlocked) {
                            Text("✓", color = Green400, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
