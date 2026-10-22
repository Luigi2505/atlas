package br.dia23.atlas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.dia23.atlas.ui.screens.exercises.ExerciseLibraryScreen
import br.dia23.atlas.ui.screens.history.CalendarHistoryScreen
import br.dia23.atlas.ui.screens.home.HomeScreen
import br.dia23.atlas.ui.screens.routine.RoutineBuilderScreen
import br.dia23.atlas.ui.screens.workout.ActiveWorkoutScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartWorkoutClick = { routineId ->
                    navController.navigate(Screen.ActiveWorkout.createRoute(routineId))
                }
            )
        }
        composable(Screen.CalendarHistory.route) {
            CalendarHistoryScreen()
        }
        composable(Screen.ExerciseLibrary.route) {
            ExerciseLibraryScreen()
        }
        composable(Screen.RoutineBuilder.route) {
            RoutineBuilderScreen(
                onRoutineSaved = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ActiveWorkout.route,
            arguments = listOf(navArgument("routineId") { type = NavType.LongType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getLong("routineId") ?: 1L
            ActiveWorkoutScreen(
                routineId = routineId,
                onFinishWorkout = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }
    }
}
