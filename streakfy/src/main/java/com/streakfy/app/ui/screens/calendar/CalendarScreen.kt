package com.streakfy.app.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.streakfy.app.data.local.entities.EventType
import com.streakfy.app.ui.theme.*
import java.util.*

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(
            DatabaseProvider.getDatabase(LocalContext.current).calendarEventDao()
        )
    )
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val events by viewModel.getEventsForDate(selectedDate.timeInMillis).collectAsState(initial = emptyList())

    val weekDays = remember { getWeekDays(selectedDate) }

    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text("Calendario", style = MaterialTheme.typography.headlineMedium, color = Foreground)

        Spacer(modifier = Modifier.height(16.dp))

        // Week View
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(weekDays) { day ->
                DayItem(
                    date = day,
                    isSelected = isSameDay(day, selectedDate),
                    onClick = {
                        selectedDate = Calendar.getInstance().apply { time = day.time }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Events for selected day
        Text(
            "Eventos del ${selectedDate.get(Calendar.DAY_OF_MONTH)}/${selectedDate.get(Calendar.MONTH) + 1}",
            color = Foreground,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            Text("No hay eventos para este día", color = Muted)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                events.forEach { event ->
                    EventItem(event = event)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add Focus Session Button
        Button(
            onClick = { /* Navigate to focus */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Purple600),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Sesión de Foco", color = Color.White)
        }
    }
}

@Composable
fun DayItem(date: Calendar, isSelected: Boolean, onClick: () -> Unit) {
    val dayName = when (date.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Lun"
        Calendar.TUESDAY -> "Mar"
        Calendar.WEDNESDAY -> "Mié"
        Calendar.THURSDAY -> "Jue"
        Calendar.FRIDAY -> "Vie"
        Calendar.SATURDAY -> "Sáb"
        Calendar.SUNDAY -> "Dom"
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = androidx.compose.ui.Modifier
            .background(
                if (isSelected) Purple600 else Card,
                CircleShape
            )
            .padding(12.dp)
            .clickable(onClick = onClick)
    ) {
        Text(dayName, color = if (isSelected) Color.White else Muted, fontSize = 12.sp)
        Text(
            date.get(Calendar.DAY_OF_MONTH).toString(),
            color = if (isSelected) Color.White else Foreground,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EventItem(event: com.streakfy.app.data.local.entities.CalendarEvent) {
    val eventColor = when (event.type) {
        EventType.WORK -> Blue600
        EventType.PERSONAL -> Purple600
        EventType.FOCUS -> Green400
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = androidx.compose.ui.Modifier
                    .size(12.dp)
                    .background(eventColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(event.title, color = Foreground, fontWeight = FontWeight.Medium)
                Text(
                    "${formatTime(event.startTime)} - ${formatTime(event.endTime)}",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

fun getWeekDays(centerDate: Calendar): List<Calendar> {
    val days = mutableListOf<Calendar>()
    val cal = centerDate.clone() as Calendar
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    for (i in 0..6) {
        days.add(cal.clone() as Calendar)
        cal.add(Calendar.DAY_OF_MONTH, 1)
    }
    return days
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun formatTime(timestamp: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return "${cal.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')}:${cal.get(Calendar.MINUTE).toString().padStart(2, '0')}"
}
