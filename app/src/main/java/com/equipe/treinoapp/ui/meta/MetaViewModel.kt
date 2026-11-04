package com.equipe.treinoapp.ui.meta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Meta
import com.equipe.treinoapp.data.repository.MetaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MetaUiState(
    val metas: List<Meta> = emptyList()
)

@HiltViewModel
class MetaViewModel @Inject constructor(
    private val repository: MetaRepository
) : ViewModel() {

    val uiState: StateFlow<MetaUiState> = repository.listar()
        .map { MetaUiState(metas = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MetaUiState()
        )

    suspend fun buscarPorId(id: Long): Meta? = repository.buscarPorId(id)

    suspend fun salvar(meta: Meta) = repository.salvar(meta)

    fun alternarConcluida(meta: Meta) {
        viewModelScope.launch { repository.salvar(meta.copy(concluida = !meta.concluida)) }
    }

    fun excluir(meta: Meta) {
        viewModelScope.launch { repository.excluir(meta) }
    }
}
