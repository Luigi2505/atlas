package br.dia23.atlas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.dia23.atlas.data.local.entity.ExerciseEntity
import br.dia23.atlas.data.local.entity.RoutineEntity
import br.dia23.atlas.data.local.entity.RoutineExerciseCrossRef
import br.dia23.atlas.data.local.entity.WorkoutSessionEntity
import br.dia23.atlas.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Exercises
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    // Routines
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :routineId")
    suspend fun getRoutineById(routineId: Long): RoutineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    // Routine Exercise Cross Ref
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExerciseCrossRef(crossRef: RoutineExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExerciseCrossRefs(crossRefs: List<RoutineExerciseCrossRef>)

    @Query("SELECT * FROM routine_exercise_cross_ref WHERE routineId = :routineId")
    fun getExercisesForRoutine(routineId: Long): Flow<List<RoutineExerciseCrossRef>>

    // Workout Sessions
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC")
    fun getAllWorkoutSessions(): Flow<List<WorkoutSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSession(session: WorkoutSessionEntity): Long

    // Workout Sets
    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId")
    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSet(set: WorkoutSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSets(sets: List<WorkoutSetEntity>)
}
