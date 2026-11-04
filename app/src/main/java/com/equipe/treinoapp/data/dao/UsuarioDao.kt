package com.equipe.treinoapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.equipe.treinoapp.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun observarPorId(id: Long): Flow<Usuario?>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun buscarPorId(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios ORDER BY nome")
    fun listarTodos(): Flow<List<Usuario>>

    @Insert
    suspend fun inserir(usuario: Usuario): Long

    @Update
    suspend fun atualizar(usuario: Usuario)
}
