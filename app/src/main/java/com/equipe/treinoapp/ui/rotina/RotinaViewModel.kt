package com.equipe.treinoapp.ui.rotina

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Rotina
import com.equipe.treinoapp.data.repository.RotinaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RotinaUiState(
    val rotinas: List<Rotina> = emptyList()
)

@HiltViewModel
class RotinaViewModel @Inject constructor(
    private val repository: RotinaRepository
) : ViewModel() {

    val uiState: StateFlow<RotinaUiState> = repository.listar()
        .map { RotinaUiState(rotinas = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RotinaUiState()
        )

    suspend fun buscarPorId(id: Long): Rotina? = repository.buscarPorId(id)

    suspend fun salvar(rotina: Rotina) = repository.salvar(rotina)

    fun excluir(rotina: Rotina) {
        viewModelScope.launch { repository.excluir(rotina) }
    }
}
