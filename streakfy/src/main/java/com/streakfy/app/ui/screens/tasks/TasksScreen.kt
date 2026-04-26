package com.streakfy.app.ui.screens.tasks

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.data.repository.TaskRepository
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.ui.Alignment
import com.streakfy.app.data.local.entities.TaskTag
import com.streakfy.app.ui.components.TaskItem

@Composable
fun TasksScreen() {
    val context = LocalContext.current

    val db = remember { DatabaseProvider.getDatabase(context) }
    val repo = remember { TaskRepository(db.taskDao()) }

    val viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repo)
    )

    val tasks by viewModel.tasks.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nueva tarea...") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        viewModel.addTask(text)
                        text = ""
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val selectedTag by viewModel.selectedTag.collectAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            FilterChip(
                selected = selectedTag == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("Todas") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            TaskTag.values().forEach { tag ->
                FilterChip(
                    selected = selectedTag == tag,
                    onClick = { viewModel.setFilter(tag) },
                    label = { Text(tag.name) }
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }
    }
}

