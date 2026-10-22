package br.dia23.atlas.ui.screens.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.dia23.atlas.ui.theme.AtlasTheme
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    routineId: Long,
    onFinishWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Active Workout (Routine: $routineId)") })
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Active Workout Stub for Routine ID: $routineId")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveWorkoutScreenPreview() {
    AtlasTheme {
        ActiveWorkoutScreen(routineId = 1L, onFinishWorkout = {})
    }
}
