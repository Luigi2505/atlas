package com.equipe.treinoapp.data.repository

import com.equipe.treinoapp.data.dao.ExercicioDao
import com.equipe.treinoapp.data.model.Exercicio
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExercicioRepository @Inject constructor(
    private val dao: ExercicioDao
) {
    fun listar(): Flow<List<Exercicio>> = dao.listar()

    fun contar(): Flow<Int> = dao.contar()

    suspend fun buscarPorId(id: Long): Exercicio? = dao.buscarPorId(id)

    suspend fun salvar(exercicio: Exercicio) {
        if (exercicio.id == 0L) dao.inserir(exercicio) else dao.atualizar(exercicio)
    }

    suspend fun excluir(exercicio: Exercicio) = dao.excluir(exercicio)
}
