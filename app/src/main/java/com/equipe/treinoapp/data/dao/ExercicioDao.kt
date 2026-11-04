package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Exercicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioDao {

    @Query("SELECT * FROM exercicios ORDER BY nome")
    fun listar(): Flow<List<Exercicio>>

    @Query("SELECT * FROM exercicios WHERE id = :id")
    suspend fun buscarPorId(id: Long): Exercicio?

    @Query("SELECT COUNT(*) FROM exercicios")
    fun contar(): Flow<Int>

    @Insert
    suspend fun inserir(exercicio: Exercicio): Long

    @Update
    suspend fun atualizar(exercicio: Exercicio)

    @Delete
    suspend fun excluir(exercicio: Exercicio)
}
