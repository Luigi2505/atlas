package com.equipe.treinoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas")
data class Meta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val descricao: String = "",
    val concluida: Boolean = false,
    val usuarioId: Long = 0
)
