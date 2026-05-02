package com.streakfy.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            DatabaseProvider.getDatabase(LocalContext.current)
        )
    )
) {
    val profile by viewModel.profile.collectAsState(initial = null)
    val unlockedAchievements by viewModel.unlockedAchievements.collectAsState(initial = emptyList())

    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Perfil", style = MaterialTheme.typography.headlineMedium, color = Foreground)

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Purple600),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    profile?.name?.firstOrNull()?.uppercase() ?: "U",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(profile?.name ?: "Usuario", color = Foreground, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(profile?.email ?: "email@streakfy.com", color = Muted, fontSize = 14.sp)
            }

            TextButton(onClick = { showEditDialog = true }) {
                Text("Editar", color = Purple600)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Level and XP
        Card(
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nivel ${profile?.level ?: 1}", color = Foreground, fontWeight = FontWeight.Bold)
                    Text("${profile?.xp ?: 0} XP", color = Purple600, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = ((profile?.xp ?: 0) % 100) / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = Purple600,
                    trackColor = Border
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text("Siguiente nivel en ${100 - ((profile?.xp ?: 0) % 100)} XP", color = Muted, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Achievements
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Logros", color = Foreground, fontWeight = FontWeight.SemiBold)
            Text("${unlockedAchievements.size}/6", color = Muted)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Achievements Grid (Simplified - showing first 6)
        val allAchievements = remember { listOf(
            "🔥" to "Racha 7 días",
            "🏆" to "Racha 30 días",
            "✅" to "100 Tareas",
            "⏱️" to "10h Foco",
            "🌅" to "Madrugador",
            "🦉" to "Nocturno"
        ) }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            allAchievements.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { (emoji, name) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Card),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(emoji, fontSize = 24.sp)
                                Text(name, color = Muted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Settings
        Card(
            colors = CardDefaults.cardColors(containerColor = Card),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notificaciones", color = Foreground)
                    Switch(
                        checked = profile?.notificationsEnabled ?: true,
                        onCheckedChange = { viewModel.toggleNotifications(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Purple600)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* Logout */ },
            colors = ButtonDefaults.buttonColors(containerColor = Red400),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentName = profile?.name ?: "",
            currentEmail = profile?.email ?: "",
            onDismiss = { showEditDialog = false },
            onSave = { name, email ->
                viewModel.updateProfile(name, email)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, email) },
                colors = ButtonDefaults.buttonColors(containerColor = Purple600)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        containerColor = Card
    )
}
