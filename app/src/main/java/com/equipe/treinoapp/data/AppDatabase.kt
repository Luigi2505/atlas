package com.equipe.treinoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.equipe.treinoapp.data.dao.ExercicioDao
import com.equipe.treinoapp.data.dao.MetaDao
import com.equipe.treinoapp.data.dao.RotinaDao
import com.equipe.treinoapp.data.dao.SerieDao
import com.equipe.treinoapp.data.dao.TreinoDao
import com.equipe.treinoapp.data.dao.UsuarioDao
import com.equipe.treinoapp.data.model.Exercicio
import com.equipe.treinoapp.data.model.Meta
import com.equipe.treinoapp.data.model.Rotina
import com.equipe.treinoapp.data.model.Serie
import com.equipe.treinoapp.data.model.Treino
import com.equipe.treinoapp.data.model.Usuario

@Database(
    entities = [
        Exercicio::class,
        Rotina::class,
        Treino::class,
        Serie::class,
        Meta::class,
        Usuario::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exercicioDao(): ExercicioDao
    abstract fun rotinaDao(): RotinaDao
    abstract fun treinoDao(): TreinoDao
    abstract fun serieDao(): SerieDao
    abstract fun metaDao(): MetaDao
    abstract fun usuarioDao(): UsuarioDao
}
