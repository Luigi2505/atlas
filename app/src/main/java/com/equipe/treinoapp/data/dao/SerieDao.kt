package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Serie
import com.equipe.treinoapp.data.model.SerieComExercicio
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao {

    @Query(
        """
        SELECT s.*, e.nome AS exercicioNome
        FROM series s
        JOIN exercicios e ON e.id = s.exercicioId
        WHERE s.usuarioId = :usuarioId
        ORDER BY s.dataMillis DESC, s.id DESC
        """
    )
    fun listarComExercicio(usuarioId: Long): Flow<List<SerieComExercicio>>

    @Query("SELECT * FROM series WHERE id = :id")
    suspend fun buscarPorId(id: Long): Serie?

    @Query("SELECT COUNT(*) FROM series WHERE usuarioId = :usuarioId")
    fun contar(usuarioId: Long): Flow<Int>

    @Insert
    suspend fun inserir(serie: Serie): Long

    @Update
    suspend fun atualizar(serie: Serie)

    @Delete
    suspend fun excluir(serie: Serie)
}
