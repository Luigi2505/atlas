package com.equipe.treinoapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "series",
    foreignKeys = [
        ForeignKey(
            entity = Exercicio::class,
            parentColumns = ["id"],
            childColumns = ["exercicioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exercicioId")]
)
data class Serie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exercicioId: Long,
    val peso: Double,
    val repeticoes: Int,
    val dataMillis: Long,
    val usuarioId: Long = 0
)

data class SerieComExercicio(
    @Embedded val serie: Serie,
    val exercicioNome: String
)
