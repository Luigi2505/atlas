package com.equipe.treinoapp.ui.serie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Exercicio
import com.equipe.treinoapp.data.model.Serie
import com.equipe.treinoapp.data.model.SerieComExercicio
import com.equipe.treinoapp.data.repository.ExercicioRepository
import com.equipe.treinoapp.data.repository.SerieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SerieUiState(
    val series: List<SerieComExercicio> = emptyList(),
    val exercicios: List<Exercicio> = emptyList(),
    val volumeTotalKg: Double = 0.0
)

@HiltViewModel
class SerieViewModel @Inject constructor(
    private val serieRepository: SerieRepository,
    private val exercicioRepository: ExercicioRepository
) : ViewModel() {

    val uiState: StateFlow<SerieUiState> = combine(
        serieRepository.listar(),
        exercicioRepository.listar()
    ) { series, exercicios ->
        SerieUiState(
            series = series,
            exercicios = exercicios,
            volumeTotalKg = series.sumOf { it.serie.peso * it.serie.repeticoes }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SerieUiState()
    )

    suspend fun buscarPorId(id: Long): Serie? = serieRepository.buscarPorId(id)

    suspend fun salvar(serie: Serie) = serieRepository.salvar(serie)

    fun excluir(serie: Serie) {
        viewModelScope.launch { serieRepository.excluir(serie) }
    }
}
