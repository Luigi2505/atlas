package com.equipe.treinoapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.equipe.treinoapp.data.AppDatabase
import com.equipe.treinoapp.data.dao.ExercicioDao
import com.equipe.treinoapp.data.dao.MetaDao
import com.equipe.treinoapp.data.dao.RotinaDao
import com.equipe.treinoapp.data.dao.SerieDao
import com.equipe.treinoapp.data.dao.TreinoDao
import com.equipe.treinoapp.data.dao.UsuarioDao
import com.equipe.treinoapp.util.hashSenha
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBanco(@ApplicationContext contexto: Context): AppDatabase {
        return Room.databaseBuilder(contexto, AppDatabase::class.java, "treino.db")
            .addCallback(dadosIniciais)
            .build()
    }

    @Provides
    fun provideExercicioDao(banco: AppDatabase): ExercicioDao = banco.exercicioDao()

    @Provides
    fun provideRotinaDao(banco: AppDatabase): RotinaDao = banco.rotinaDao()

    @Provides
    fun provideTreinoDao(banco: AppDatabase): TreinoDao = banco.treinoDao()

    @Provides
    fun provideSerieDao(banco: AppDatabase): SerieDao = banco.serieDao()

    @Provides
    fun provideMetaDao(banco: AppDatabase): MetaDao = banco.metaDao()

    @Provides
    fun provideUsuarioDao(banco: AppDatabase): UsuarioDao = banco.usuarioDao()

    private val dadosIniciais = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val exercicios = listOf(
                Triple("Supino reto", "Peito", "Barra"),
                Triple("Supino inclinado", "Peito", "Halteres"),
                Triple("Puxada frontal", "Costas", "Polia"),
                Triple("Remada curvada", "Costas", "Barra"),
                Triple("Desenvolvimento", "Ombros", "Halteres"),
                Triple("Rosca direta", "Bíceps", "Barra"),
                Triple("Tríceps corda", "Tríceps", "Polia"),
                Triple("Agachamento livre", "Pernas", "Barra"),
                Triple("Leg press", "Pernas", "Máquina"),
                Triple("Abdominal", "Abdômen", "Peso do corpo")
            )
            exercicios.forEach { (nome, grupo, equipamento) ->
                db.execSQL(
                    "INSERT INTO exercicios (nome, grupoMuscular, equipamento) VALUES (?, ?, ?)",
                    arrayOf<Any?>(nome, grupo, equipamento)
                )
            }
            db.execSQL(
                "INSERT INTO usuarios (nome, email, senhaHash, telefone, objetivo) VALUES (?, ?, ?, ?, ?)",
                arrayOf<Any?>(
                    "Aluno Teste",
                    "aluno@academia.com",
                    hashSenha("123456"),
                    "(41) 99999-0000",
                    "Ganho de massa"
                )
            )
            db.execSQL(
                "INSERT INTO rotinas (nome, diaSemana, observacoes, usuarioId) VALUES (?, ?, ?, ?)",
                arrayOf<Any?>("Push Day", "Segunda", "Peito, ombros e tríceps", 1)
            )
            db.execSQL(
                "INSERT INTO metas (titulo, descricao, concluida, usuarioId) VALUES (?, ?, ?, ?)",
                arrayOf<Any?>("Treinar 4x por semana", "Manter a frequência durante o mês", 0, 1)
            )
        }
    }
}
