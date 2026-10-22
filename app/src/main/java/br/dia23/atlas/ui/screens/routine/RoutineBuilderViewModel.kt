package br.dia23.atlas.ui.screens.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dia23.atlas.data.local.dao.AppDao
import br.dia23.atlas.data.local.entity.RoutineEntity
import br.dia23.atlas.data.local.entity.RoutineExerciseCrossRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineBuilderViewModel @Inject constructor(
    private val dao: AppDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineBuilderUiState(isLoading = true))
    val uiState: StateFlow<RoutineBuilderUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            dao.getAllExercises().collect { exercisesList ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        exercises = exercisesList
                    )
                }
            }
        }
    }

    fun updateRoutineName(name: String) {
        _uiState.update { it.copy(routineName = name) }
    }

    fun toggleExerciseSelection(exerciseId: Long) {
        _uiState.update { state ->
            val currentSelected = state.selectedExerciseIds.toMutableList()
            if (currentSelected.contains(exerciseId)) {
                currentSelected.remove(exerciseId)
            } else {
                currentSelected.add(exerciseId)
            }
            state.copy(selectedExerciseIds = currentSelected)
        }
    }

    fun saveRoutine(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.routineName.isBlank()) return

        viewModelScope.launch {
            val routine = RoutineEntity(name = currentState.routineName)
            val routineId = dao.insertRoutine(routine)

            currentState.selectedExerciseIds.forEachIndexed { index, exerciseId ->
                val crossRef = RoutineExerciseCrossRef(
                    routineId = routineId,
                    exerciseId = exerciseId,
                    orderIndex = index,
                    targetSets = 3
                )
                dao.insertRoutineExerciseCrossRef(crossRef)
            }

            _uiState.update { it.copy(isSaved = true) }
            onSuccess()
        }
    }
}
