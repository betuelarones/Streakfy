package com.streakfy.app.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun StatisticsScreen(
    viewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModelFactory(
            DatabaseProvider.getDatabase(LocalContext.current)
        )
    )
) {
    val totalFocusTime by viewModel.totalFocusTime.collectAsState(initial = 0)
    val completedTasks by viewModel.completedTasks.collectAsState(initial = 0)
    val productiveDays by viewModel.productiveDays.collectAsState(initial = 0)
    val productivityScore by viewModel.productivityScore.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Estadísticas", style = MaterialTheme.typography.headlineMedium, color = Foreground)

        Spacer(modifier = Modifier.height(16.dp))

        // Top Stats Cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                title = "Tiempo Total",
                value = "${totalFocusTime}m",
                modifier = Modifier.weight(1f),
                color = Blue600
            )
            StatCard(
                title = "Tareas",
                value = "$completedTasks",
                modifier = Modifier.weight(1f),
                color = Green400
            )
            StatCard(
                title = "Días",
                value = "$productiveDays",
                modifier = Modifier.weight(1f),
                color = Purple600
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Productivity Score
        Card(
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Puntuación Semanal", color = Foreground, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$productivityScore%", color = Green400, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("Productividad", color = Muted, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weekly Progress Bar Chart (Simplified)
        Text("Horas de foco (semanal)", color = Foreground, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEachIndexed { index, day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((20 + index * 15).dp)
                            .background(Blue600, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(day, color = Muted, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Category Distribution (Simplified)
        Text("Distribución por categoría", color = Foreground, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryBar("Trabajo", 0.4f, Blue600)
            CategoryBar("Estudio", 0.35f, Purple600)
            CategoryBar("Personal", 0.25f, Green400)
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
fun CategoryBar(category: String, percentage: Float, color: Color) {
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(category, color = Foreground, fontSize = 12.sp)
            Text("${(percentage * 100).toInt()}%", color = Muted, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Border, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .height(8.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}
