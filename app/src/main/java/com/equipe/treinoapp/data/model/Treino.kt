package com.equipe.treinoapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "treinos",
    foreignKeys = [
        ForeignKey(
            entity = Rotina::class,
            parentColumns = ["id"],
            childColumns = ["rotinaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("rotinaId")]
)
data class Treino(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rotinaId: Long?,
    val dataMillis: Long,
    val duracaoMinutos: Int,
    val observacoes: String = "",
    val usuarioId: Long = 0
)

data class TreinoComRotina(
    @Embedded val treino: Treino,
    val rotinaNome: String?
)
