package com.streakfy.app.data.local.dao

import androidx.room.*
import com.streakfy.app.data.local.entities.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfile?

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfileFlow(): Flow<UserProfile?> = flow {
        emit(getProfile())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile)

    @Update
    suspend fun update(profile: UserProfile)
}
