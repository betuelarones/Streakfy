package com.streakfy.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streakfy.app.data.local.entities.Priority
import com.streakfy.app.data.local.entities.Task
import com.streakfy.app.data.local.entities.TaskTag
import com.streakfy.app.ui.theme.*

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (() -> Unit)? = null
) {
    val priorityColor = when (task.priority) {
        Priority.HIGH -> Red400
        Priority.MEDIUM -> Yellow400
        Priority.LOW -> Green400
    }

    val tagColor = when (task.tag) {
        TaskTag.WORK -> Blue600
        TaskTag.PERSONAL -> Purple600
        TaskTag.STUDY -> Green400
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Green400)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.completed) Muted else Foreground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Priority Badge
                    Box(
                        modifier = Modifier
                            .background(priorityColor.copy(alpha = 0.2f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            when (task.priority) {
                                Priority.HIGH -> "Alta"
                                Priority.MEDIUM -> "Media"
                                Priority.LOW -> "Baja"
                            },
                            color = priorityColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Tag Badge
                    Box(
                        modifier = Modifier
                            .background(tagColor.copy(alpha = 0.2f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            when (task.tag) {
                                TaskTag.WORK -> "Trabajo"
                                TaskTag.PERSONAL -> "Personal"
                                TaskTag.STUDY -> "Estudio"
                            },
                            color = tagColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            TextButton(onClick = onDelete) {
                Text("Eliminar", color = Red400, fontSize = 12.sp)
            }
        }
    }
}