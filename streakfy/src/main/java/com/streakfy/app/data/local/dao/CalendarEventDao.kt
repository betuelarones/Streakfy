package com.streakfy.app.data.local.dao

import androidx.room.*
import com.streakfy.app.data.local.entities.CalendarEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {

    @Query("SELECT * FROM calendar_events WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY startTime ASC")
    fun getByDate(date: Long): Flow<List<CalendarEvent>>

    @Query("SELECT * FROM calendar_events ORDER BY startTime ASC")
    fun getAll(): Flow<List<CalendarEvent>>

    @Insert
    suspend fun insert(event: CalendarEvent)

    @Update
    suspend fun update(event: CalendarEvent)

    @Delete
    suspend fun delete(event: CalendarEvent)
}
