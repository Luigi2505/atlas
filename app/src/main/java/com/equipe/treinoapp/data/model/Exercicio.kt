package com.equipe.treinoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercicios")
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val grupoMuscular: String,
    val equipamento: String,
    val usuarioId: Long? = null
)
