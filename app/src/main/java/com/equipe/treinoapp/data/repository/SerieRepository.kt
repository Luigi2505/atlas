package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.dao.SerieDao
import com.equipe.treinoapp.data.model.Serie
import com.equipe.treinoapp.data.model.SerieComExercicio
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SerieRepository @Inject constructor(
    private val dao: SerieDao,
    private val sessao: SessaoManager
) {
    private fun donoAtual(): Long = sessao.usuarioId.value ?: -1L

    fun listar(): Flow<List<SerieComExercicio>> =
        sessao.usuarioId.flatMapLatest { id -> dao.listarComExercicio(id ?: -1L) }

    fun contar(): Flow<Int> =
        sessao.usuarioId.flatMapLatest { id -> dao.contar(id ?: -1L) }

    suspend fun buscarPorId(id: Long): Serie? = dao.buscarPorId(id)

    suspend fun salvar(item: Serie) {
        val comDono = item.copy(usuarioId = donoAtual())
        if (item.id == 0L) dao.inserir(comDono) else dao.atualizar(comDono)
    }

    suspend fun excluir(item: Serie) = dao.excluir(item)
}
