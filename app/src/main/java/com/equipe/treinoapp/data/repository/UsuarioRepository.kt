package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.dao.UsuarioDao
import com.equipe.treinoapp.data.model.Usuario
import com.equipe.treinoapp.util.hashSenha
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val dao: UsuarioDao
) {
    fun observarPorId(id: Long): Flow<Usuario?> = dao.observarPorId(id)

    suspend fun login(email: String, senha: String): Usuario? {
        val usuario = dao.buscarPorEmail(email.trim().lowercase()) ?: return null
        return if (usuario.senhaHash == hashSenha(senha)) usuario else null
    }

    suspend fun cadastrar(
        nome: String,
        email: String,
        telefone: String,
        objetivo: String,
        senha: String
    ): Long? {
        val emailNormalizado = email.trim().lowercase()
        if (dao.buscarPorEmail(emailNormalizado) != null) return null
        return dao.inserir(
            Usuario(
                nome = nome.trim(),
                email = emailNormalizado,
                senhaHash = hashSenha(senha),
                telefone = telefone.trim(),
                objetivo = objetivo
            )
        )
    }

    suspend fun salvarPerfil(
        id: Long,
        nome: String,
        email: String,
        telefone: String,
        objetivo: String,
        novaSenha: String
    ): Boolean {
        val atual = dao.buscarPorId(id) ?: return false
        val emailNormalizado = email.trim().lowercase()
        val outro = dao.buscarPorEmail(emailNormalizado)
        if (outro != null && outro.id != id) return false

        dao.atualizar(
            atual.copy(
                nome = nome.trim(),
                email = emailNormalizado,
                telefone = telefone.trim(),
                objetivo = objetivo,
                senhaHash = if (novaSenha.isBlank()) atual.senhaHash else hashSenha(novaSenha)
            )
        )
        return true
    }
}
