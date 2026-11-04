package com.equipe.treinoapp.ui.exercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Exercicio
import com.equipe.treinoapp.data.repository.ExercicioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExercicioUiState(
    val exercicios: List<Exercicio> = emptyList(),
    val busca: String = ""
)

@HiltViewModel
class ExercicioViewModel @Inject constructor(
    private val repository: ExercicioRepository
) : ViewModel() {

    private val busca = MutableStateFlow("")

    val uiState: StateFlow<ExercicioUiState> = combine(repository.listar(), busca) { lista, texto ->
        val filtrada = if (texto.isBlank()) lista
        else lista.filter { it.nome.contains(texto, ignoreCase = true) }
        ExercicioUiState(exercicios = filtrada, busca = texto)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExercicioUiState()
    )

    fun onBuscaChange(texto: String) {
        busca.value = texto
    }

    suspend fun buscarPorId(id: Long): Exercicio? = repository.buscarPorId(id)

    suspend fun salvar(exercicio: Exercicio) = repository.salvar(exercicio)

    fun excluir(exercicio: Exercicio) {
        viewModelScope.launch { repository.excluir(exercicio) }
    }
}
