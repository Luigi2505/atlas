package com.equipe.treinoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rotinas")
data class Rotina(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val diaSemana: String,
    val observacoes: String = "",
    val usuarioId: Long = 0
)
