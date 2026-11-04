package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.dao.RotinaDao
import com.equipe.treinoapp.data.model.Rotina
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class RotinaRepository @Inject constructor(
    private val dao: RotinaDao,
    private val sessao: SessaoManager
) {
    private fun donoAtual(): Long = sessao.usuarioId.value ?: -1L

    fun listar(): Flow<List<Rotina>> =
        sessao.usuarioId.flatMapLatest { id -> dao.listar(id ?: -1L) }

    fun contar(): Flow<Int> =
        sessao.usuarioId.flatMapLatest { id -> dao.contar(id ?: -1L) }

    suspend fun buscarPorId(id: Long): Rotina? = dao.buscarPorId(id)

    suspend fun salvar(item: Rotina) {
        val comDono = item.copy(usuarioId = donoAtual())
        if (item.id == 0L) dao.inserir(comDono) else dao.atualizar(comDono)
    }

    suspend fun excluir(item: Rotina) = dao.excluir(item)
}
