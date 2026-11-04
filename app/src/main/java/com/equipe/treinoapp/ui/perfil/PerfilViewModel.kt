package com.equipe.treinoapp.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.model.Usuario
import com.equipe.treinoapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class PerfilUiState(
    val usuario: Usuario? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val sessao: SessaoManager
) : ViewModel() {

    val uiState: StateFlow<PerfilUiState> = sessao.usuarioId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.observarPorId(id)
        }
        .map { PerfilUiState(usuario = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PerfilUiState()
        )

    suspend fun salvar(
        nome: String,
        email: String,
        telefone: String,
        objetivo: String,
        novaSenha: String
    ): Boolean {
        val id = sessao.usuarioId.value ?: return false
        return repository.salvarPerfil(id, nome, email, telefone, objetivo, novaSenha)
    }

    fun sair() {
        sessao.sair()
    }
}
