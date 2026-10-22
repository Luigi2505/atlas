package br.dia23.atlas.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.dia23.atlas.data.local.dao.AppDao
import br.dia23.atlas.data.local.entity.ExerciseEntity
import br.dia23.atlas.data.local.entity.RoutineEntity
import br.dia23.atlas.data.local.entity.RoutineExerciseCrossRef
import br.dia23.atlas.data.local.entity.WorkoutSessionEntity
import br.dia23.atlas.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ExerciseEntity::class,
        RoutineEntity::class,
        RoutineExerciseCrossRef::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "atlas_workout_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateExercises(database.appDao())
                }
            }
        }

        private suspend fun prepopulateExercises(dao: AppDao) {
            val defaultExercises = listOf(
                ExerciseEntity(name = "Supino", muscleGroup = "Peito", defaultRestSeconds = 90),
                ExerciseEntity(name = "Agachamento", muscleGroup = "Pernas", defaultRestSeconds = 120),
                ExerciseEntity(name = "Barra Fixa", muscleGroup = "Costas", defaultRestSeconds = 90),
                ExerciseEntity(name = "Desenvolvimento", muscleGroup = "Ombros", defaultRestSeconds = 90),
                ExerciseEntity(name = "Rosca Direta", muscleGroup = "Bíceps", defaultRestSeconds = 60)
            )
            dao.insertExercises(defaultExercises)
        }
    }
}
