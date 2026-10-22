package br.dia23.atlas.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CalendarHistory : Screen("history")
    object ExerciseLibrary : Screen("exercises")
    object RoutineBuilder : Screen("routine_builder")
    
    object ActiveWorkout : Screen("active_workout/{routineId}") {
        fun createRoute(routineId: Long): String = "active_workout/$routineId"
    }
}
