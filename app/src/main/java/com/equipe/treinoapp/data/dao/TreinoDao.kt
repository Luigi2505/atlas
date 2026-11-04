package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Treino
import com.equipe.treinoapp.data.model.TreinoComRotina
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {

    @Query(
        """
        SELECT t.*, r.nome AS rotinaNome
        FROM treinos t
        LEFT JOIN rotinas r ON r.id = t.rotinaId
        WHERE t.usuarioId = :usuarioId
        ORDER BY t.dataMillis DESC, t.id DESC
        """
    )
    fun listarComRotina(usuarioId: Long): Flow<List<TreinoComRotina>>

    @Query("SELECT * FROM treinos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Treino?

    @Query("SELECT COUNT(*) FROM treinos WHERE usuarioId = :usuarioId")
    fun contar(usuarioId: Long): Flow<Int>

    @Insert
    suspend fun inserir(treino: Treino): Long

    @Update
    suspend fun atualizar(treino: Treino)

    @Delete
    suspend fun excluir(treino: Treino)
}
