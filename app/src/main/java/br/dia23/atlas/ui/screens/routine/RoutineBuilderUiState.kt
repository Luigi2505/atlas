package br.dia23.atlas.ui.screens.routine

import br.dia23.atlas.data.local.entity.ExerciseEntity

data class RoutineBuilderUiState(
    val isLoading: Boolean = false,
    val exercises: List<ExerciseEntity> = emptyList(),
    val routineName: String = "",
    val selectedExerciseIds: List<Long> = emptyList(),
    val isSaved: Boolean = false
)
