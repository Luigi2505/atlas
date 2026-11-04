package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.dao.MetaDao
import com.equipe.treinoapp.data.model.Meta
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MetaRepository @Inject constructor(
    private val dao: MetaDao,
    private val sessao: SessaoManager
) {
    private fun donoAtual(): Long = sessao.usuarioId.value ?: -1L

    fun listar(): Flow<List<Meta>> =
        sessao.usuarioId.flatMapLatest { id -> dao.listar(id ?: -1L) }

    fun contarPendentes(): Flow<Int> =
        sessao.usuarioId.flatMapLatest { id -> dao.contarPendentes(id ?: -1L) }

    suspend fun buscarPorId(id: Long): Meta? = dao.buscarPorId(id)

    suspend fun salvar(item: Meta) {
        val comDono = item.copy(usuarioId = donoAtual())
        if (item.id == 0L) dao.inserir(comDono) else dao.atualizar(comDono)
    }

    suspend fun excluir(item: Meta) = dao.excluir(item)
}
