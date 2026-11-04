package com.equipe.treinoapp.ui.inicio

import androidx.lifecycle.ViewModel
import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val sessao: SessaoManager
) : ViewModel() {

    suspend fun entrar(email: String, senha: String): Boolean {
        val usuario = repository.login(email, senha) ?: return false
        sessao.entrar(usuario.id)
        return true
    }

    suspend fun cadastrar(
        nome: String,
        email: String,
        telefone: String,
        objetivo: String,
        senha: String
    ): Boolean {
        val id = repository.cadastrar(nome, email, telefone, objetivo, senha) ?: return false
        sessao.entrar(id)
        return true
    }
}
