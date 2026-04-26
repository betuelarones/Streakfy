package com.app.myapplication.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.myapplication.data.local.database.DatabaseProvider
import com.app.myapplication.data.repository.TaskRepository
import androidx.compose.foundation.lazy.items

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
        Row {
            TextField(
                value = text,
                onValueChange = { text = it},
                modifier = Modifier.weight(1f),
                placeholder = { Text("Nueva tarea") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (text.isNotBlank()){
                    viewModel.addTask(text)
                        text = ""
                    }
            }) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(task.title)

                    Row {
                        Checkbox(
                            checked = task.completed,
                            onCheckedChange = {
                                viewModel.toggleTask(task)
                            }
                        )

                        Button(onClick = {
                            viewModel.delete(task)
                        }) {
                            Text("X")
                        }
                    }
                }
            }
        }
    }

}