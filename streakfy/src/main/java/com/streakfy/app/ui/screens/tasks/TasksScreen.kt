package com.streakfy.app.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streakfy.app.data.local.database.DatabaseProvider
import com.streakfy.app.data.local.entities.Priority
import com.streakfy.app.data.local.entities.Task
import com.streakfy.app.data.local.entities.TaskTag
import com.streakfy.app.ui.components.TaskItem
import com.streakfy.app.ui.theme.*

@Composable
fun TasksScreen() {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val repo = remember { com.streakfy.app.data.repository.TaskRepository(db.taskDao()) }
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(repo))

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val selectedTag by viewModel.selectedTag.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Tareas", style = MaterialTheme.typography.headlineMedium, color = Foreground)

        Spacer(modifier = Modifier.height(12.dp))

        // Filters
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTag == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("Todas") }
            )
            TaskTag.values().forEach { tag ->
                FilterChip(
                    selected = selectedTag == tag,
                    onClick = { viewModel.setFilter(tag) },
                    label = {
                        Text(
                            when (tag) {
                                TaskTag.WORK -> "Trabajo"
                                TaskTag.PERSONAL -> "Personal"
                                TaskTag.STUDY -> "Estudio"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(tasks, key = { task -> task.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) },
                    onDelete = { viewModel.deleteTask(task) },
                    onEdit = { editingTask = task }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Task Button
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Purple600),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Nueva Tarea", color = androidx.compose.ui.graphics.Color.White)
        }
    }

    // Add/Edit Task Dialog
    if (showAddDialog || editingTask != null) {
        TaskDialog(
            task = editingTask,
            onDismiss = {
                showAddDialog = false
                editingTask = null
            },
            onSave = { title, priority, tag ->
                if (editingTask != null) {
                    viewModel.updateTask(editingTask!!.copy(title = title, priority = priority, tag = tag))
                } else {
                    viewModel.addTask(title, priority, tag)
                }
                showAddDialog = false
                editingTask = null
            }
        )
    }
}

@Composable
fun TaskDialog(
    task: Task? = null,
    onDismiss: () -> Unit,
    onSave: (String, Priority, TaskTag) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var selectedPriority by remember { mutableStateOf(task?.priority ?: Priority.MEDIUM) }
    var selectedTag by remember { mutableStateOf(task?.tag ?: TaskTag.PERSONAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "Nueva Tarea" else "Editar Tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Prioridad:", color = Foreground)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.values().forEach { priority ->
                        FilterChip(
                            selected = selectedPriority == priority,
                            onClick = { selectedPriority = priority },
                            label = {
                                Text(
                                    when (priority) {
                                        Priority.HIGH -> "Alta"
                                        Priority.MEDIUM -> "Media"
                                        Priority.LOW -> "Baja"
                                    }
                                )
                            }
                        )
                    }
                }

                Text("Categoría:", color = Foreground)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskTag.values().forEach { tag ->
                        FilterChip(
                            selected = selectedTag == tag,
                            onClick = { selectedTag = tag },
                            label = {
                                Text(
                                    when (tag) {
                                        TaskTag.WORK -> "Trabajo"
                                        TaskTag.PERSONAL -> "Personal"
                                        TaskTag.STUDY -> "Estudio"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onSave(title, selectedPriority, selectedTag) },
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
