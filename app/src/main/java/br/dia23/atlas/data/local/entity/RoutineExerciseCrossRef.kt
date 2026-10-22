package br.dia23.atlas.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "routine_exercise_cross_ref",
    primaryKeys = ["routineId", "exerciseId"]
)
data class RoutineExerciseCrossRef(
    val routineId: Long,
    val exerciseId: Long,
    val orderIndex: Int,
    val targetSets: Int = 3
)
