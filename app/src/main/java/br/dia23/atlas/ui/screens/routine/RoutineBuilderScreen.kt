package br.dia23.atlas.ui.screens.routine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.dia23.atlas.data.local.entity.ExerciseEntity
import br.dia23.atlas.ui.theme.AtlasTheme
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBuilderScreen(
    onRoutineSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutineBuilderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoutineBuilderContent(
        uiState = uiState,
        onRoutineNameChanged = viewModel::updateRoutineName,
        onExerciseToggle = viewModel::toggleExerciseSelection,
        onSaveRoutine = { viewModel.saveRoutine(onRoutineSaved) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBuilderContent(
    uiState: RoutineBuilderUiState,
    onRoutineNameChanged: (String) -> Unit,
    onExerciseToggle: (Long) -> Unit,
    onSaveRoutine: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Build Routine") })
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onSaveRoutine,
                    enabled = uiState.routineName.isNotBlank() && uiState.selectedExerciseIds.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Save Routine")
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.routineName,
                onValueChange = onRoutineNameChanged,
                label = { Text("Routine Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select Exercises",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.exercises) { exercise ->
                        val isSelected = uiState.selectedExerciseIds.contains(exercise.id)
                        ExerciseSelectionItem(
                            exercise = exercise,
                            isSelected = isSelected,
                            onToggle = { onExerciseToggle(exercise.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseSelectionItem(
    exercise: ExerciseEntity,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = exercise.muscleGroup,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineBuilderScreenPreview() {
    AtlasTheme {
        RoutineBuilderContent(
            uiState = RoutineBuilderUiState(
                routineName = "Push Day",
                exercises = listOf(
                    ExerciseEntity(id = 1L, name = "Supino", muscleGroup = "Peito"),
                    ExerciseEntity(id = 2L, name = "Desenvolvimento", muscleGroup = "Ombros")
                ),
                selectedExerciseIds = listOf(1L)
            ),
            onRoutineNameChanged = {},
            onExerciseToggle = {},
            onSaveRoutine = {}
        )
    }
}
