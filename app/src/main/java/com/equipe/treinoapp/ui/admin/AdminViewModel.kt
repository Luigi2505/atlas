package com.equipe.treinoapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.model.Usuario
import com.equipe.treinoapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.equipe.treinoapp.data.SessaoManager

data class AdminUiState(
    val usuarios: List<Usuario> = emptyList(),
    val idUsuarioLogado: Long? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val sessao: SessaoManager
) : ViewModel() {

    val uiState: StateFlow<AdminUiState> = combine(
        repository.listarTodos(),
        sessao.usuarioId
    ) { usuarios, logadoId ->
        AdminUiState(usuarios = usuarios, idUsuarioLogado = logadoId)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AdminUiState()
        )

    fun mudarRole(id: Long, novaRole: String) {
        viewModelScope.launch {
            repository.atualizarRole(id, novaRole)
        }
    }
}
