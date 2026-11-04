package com.equipe.treinoapp.ui.treino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Rotina
import com.equipe.treinoapp.data.model.Treino
import com.equipe.treinoapp.data.model.TreinoComRotina
import com.equipe.treinoapp.data.repository.RotinaRepository
import com.equipe.treinoapp.data.repository.TreinoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TreinoUiState(
    val treinos: List<TreinoComRotina> = emptyList(),
    val rotinas: List<Rotina> = emptyList()
)

@HiltViewModel
class TreinoViewModel @Inject constructor(
    private val treinoRepository: TreinoRepository,
    private val rotinaRepository: RotinaRepository
) : ViewModel() {

    val uiState: StateFlow<TreinoUiState> = combine(
        treinoRepository.listar(),
        rotinaRepository.listar()
    ) { treinos, rotinas ->
        TreinoUiState(treinos = treinos, rotinas = rotinas)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TreinoUiState()
    )

    suspend fun buscarPorId(id: Long): Treino? = treinoRepository.buscarPorId(id)

    suspend fun salvar(treino: Treino) = treinoRepository.salvar(treino)

    fun excluir(treino: Treino) {
        viewModelScope.launch { treinoRepository.excluir(treino) }
    }
}
