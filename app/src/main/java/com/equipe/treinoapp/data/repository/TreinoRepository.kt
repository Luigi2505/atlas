package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.SessaoManager
import com.equipe.treinoapp.data.dao.TreinoDao
import com.equipe.treinoapp.data.model.Treino
import com.equipe.treinoapp.data.model.TreinoComRotina
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class TreinoRepository @Inject constructor(
    private val dao: TreinoDao,
    private val sessao: SessaoManager
) {
    private fun donoAtual(): Long = sessao.usuarioId.value ?: -1L

    fun listar(): Flow<List<TreinoComRotina>> =
        sessao.usuarioId.flatMapLatest { id -> dao.listarComRotina(id ?: -1L) }

    fun contar(): Flow<Int> =
        sessao.usuarioId.flatMapLatest { id -> dao.contar(id ?: -1L) }

    suspend fun buscarPorId(id: Long): Treino? = dao.buscarPorId(id)

    suspend fun salvar(item: Treino) {
        val comDono = item.copy(usuarioId = donoAtual())
        if (item.id == 0L) dao.inserir(comDono) else dao.atualizar(comDono)
    }

    suspend fun excluir(item: Treino) = dao.excluir(item)
}
