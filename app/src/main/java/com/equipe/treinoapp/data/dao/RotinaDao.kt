package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Rotina
import kotlinx.coroutines.flow.Flow

@Dao
interface RotinaDao {

    @Query("SELECT * FROM rotinas WHERE usuarioId = :usuarioId ORDER BY nome")
    fun listar(usuarioId: Long): Flow<List<Rotina>>

    @Query("SELECT * FROM rotinas WHERE id = :id")
    suspend fun buscarPorId(id: Long): Rotina?

    @Query("SELECT COUNT(*) FROM rotinas WHERE usuarioId = :usuarioId")
    fun contar(usuarioId: Long): Flow<Int>

    @Insert
    suspend fun inserir(rotina: Rotina): Long

    @Update
    suspend fun atualizar(rotina: Rotina)

    @Delete
    suspend fun excluir(rotina: Rotina)
}
