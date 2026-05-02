package com.streakfy.app.ui.screens.focus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.data.repository.FocusSessionRepository
import com.streakfy.app.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun FocusScreen(
    onBack: () -> Unit,
    viewModel: FocusViewModel = viewModel(
        factory = com.streakfy.app.ui.screens.focus.FocusViewModelFactory(
            FocusSessionRepository(DatabaseProvider.getDatabase(LocalContext.current).focusSessionDao())
        )
    )
) {
    val minutes = viewModel.timeLeft / 60
    val seconds = viewModel.timeLeft % 60
    val progress = if (viewModel.totalTime > 0) {
        (viewModel.totalTime - viewModel.timeLeft).toFloat() / viewModel.totalTime.toFloat()
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Timer with Circular Progress
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(250.dp),
                color = Purple600,
                strokeWidth = 8.dp,
                strokeCap = StrokeCap.Round
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Foreground
                )
                Text(
                    if (viewModel.isRunning) "En progreso..." else "Pausado",
                    color = Muted,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Presets
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(15, 25, 45).forEach { minutes ->
                OutlinedButton(
                    onClick = { viewModel.setPreset(minutes) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (viewModel.totalTime == minutes * 60) Purple600 else Muted
                    ),
                    shape = CircleShape
                ) {
                    Text("${minutes}m")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!viewModel.isRunning) {
                Button(
                    onClick = { viewModel.start() },
                    colors = ButtonDefaults.buttonColors(containerColor = Green400),
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Text("▶", fontSize = 24.sp, color = Color.White)
                }
            } else {
                Button(
                    onClick = { viewModel.pause() },
                    colors = ButtonDefaults.buttonColors(containerColor = Yellow400),
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Text("⏸", fontSize = 24.sp, color = Color.White)
                }
            }

            Button(
                onClick = { viewModel.reset() },
                colors = ButtonDefaults.buttonColors(containerColor = Red400),
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Text("↺", fontSize = 24.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextButton(onClick = onBack) {
            Text("Volver", color = Muted)
        }
    }
}