package br.dia23.atlas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val routineId: Long,
    val startTime: Long,
    val endTime: Long?,
    val date: Long
)
