package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Meta
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDao {

    @Query("SELECT * FROM metas WHERE usuarioId = :usuarioId ORDER BY concluida, id DESC")
    fun listar(usuarioId: Long): Flow<List<Meta>>

    @Query("SELECT * FROM metas WHERE id = :id")
    suspend fun buscarPorId(id: Long): Meta?

    @Query("SELECT COUNT(*) FROM metas WHERE concluida = 0 AND usuarioId = :usuarioId")
    fun contarPendentes(usuarioId: Long): Flow<Int>

    @Insert
    suspend fun inserir(meta: Meta): Long

    @Update
    suspend fun atualizar(meta: Meta)

    @Delete
    suspend fun excluir(meta: Meta)
}
