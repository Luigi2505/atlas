package com.equipe.treinoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.equipe.treinoapp.ui.exercicio.ExercicioFormScreen
import com.equipe.treinoapp.ui.exercicio.ExercicioListScreen
import com.equipe.treinoapp.ui.home.HomeScreen
import com.equipe.treinoapp.ui.inicio.BoasVindasScreen
import com.equipe.treinoapp.ui.inicio.CadastroScreen
import com.equipe.treinoapp.ui.inicio.LoginScreen
import com.equipe.treinoapp.ui.meta.MetaFormScreen
import com.equipe.treinoapp.ui.meta.MetaListScreen
import com.equipe.treinoapp.ui.perfil.PerfilScreen
import com.equipe.treinoapp.ui.rotina.RotinaFormScreen
import com.equipe.treinoapp.ui.rotina.RotinaListScreen
import com.equipe.treinoapp.ui.serie.SerieFormScreen
import com.equipe.treinoapp.ui.serie.SerieListScreen
import com.equipe.treinoapp.ui.treino.TreinoFormScreen
import com.equipe.treinoapp.ui.treino.TreinoListScreen

object Rotas {
    const val BOAS_VINDAS = "boas_vindas"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val PERFIL = "perfil"

    const val HOME = "home"

    const val EXERCICIOS = "exercicios"
    const val EXERCICIO_FORM = "exercicio_form/{id}"

    const val ROTINAS = "rotinas"
    const val ROTINA_FORM = "rotina_form/{id}"

    const val TREINOS = "treinos"
    const val TREINO_FORM = "treino_form/{id}"

    const val SERIES = "series"
    const val SERIE_FORM = "serie_form/{id}"

    const val METAS = "metas"
    const val META_FORM = "meta_form/{id}"

    fun exercicioForm(id: Long = -1L) = "exercicio_form/$id"
    fun rotinaForm(id: Long = -1L) = "rotina_form/$id"
    fun treinoForm(id: Long = -1L) = "treino_form/$id"
    fun serieForm(id: Long = -1L) = "serie_form/$id"
    fun metaForm(id: Long = -1L) = "meta_form/$id"
}

private val argumentoId = listOf(navArgument("id") { type = NavType.LongType })

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val voltar: () -> Unit = { navController.popBackStack() }

    val irParaHome: () -> Unit = {
        navController.navigate(Rotas.HOME) {
            popUpTo(Rotas.BOAS_VINDAS) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Rotas.BOAS_VINDAS) {

        composable(Rotas.BOAS_VINDAS) {
            BoasVindasScreen(
                onEntrar = { navController.navigate(Rotas.LOGIN) },
                onCadastrar = { navController.navigate(Rotas.CADASTRO) }
            )
        }
        composable(Rotas.LOGIN) {
            LoginScreen(
                onLoginOk = irParaHome,
                onIrCadastro = { navController.navigate(Rotas.CADASTRO) },
                onVoltar = voltar
            )
        }
        composable(Rotas.CADASTRO) {
            CadastroScreen(
                onCadastroOk = irParaHome,
                onIrLogin = { navController.navigate(Rotas.LOGIN) },
                onVoltar = voltar
            )
        }

        composable(Rotas.PERFIL) {
            PerfilScreen(
                onVoltar = voltar,
                onSair = {
                    navController.navigate(Rotas.BOAS_VINDAS) {
                        popUpTo(Rotas.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Rotas.HOME) {
            HomeScreen(
                onAbrirExercicios = { navController.navigate(Rotas.EXERCICIOS) },
                onAbrirRotinas = { navController.navigate(Rotas.ROTINAS) },
                onAbrirTreinos = { navController.navigate(Rotas.TREINOS) },
                onAbrirSeries = { navController.navigate(Rotas.SERIES) },
                onAbrirMetas = { navController.navigate(Rotas.METAS) },
                onAbrirPerfil = { navController.navigate(Rotas.PERFIL) }
            )
        }

        composable(Rotas.EXERCICIOS) {
            ExercicioListScreen(
                onNovo = { navController.navigate(Rotas.exercicioForm()) },
                onEditar = { id -> navController.navigate(Rotas.exercicioForm(id)) },
                onVoltar = voltar
            )
        }
        composable(Rotas.EXERCICIO_FORM, arguments = argumentoId) { entrada ->
            ExercicioFormScreen(
                id = entrada.arguments?.getLong("id") ?: -1L,
                onVoltar = voltar
            )
        }

        composable(Rotas.ROTINAS) {
            RotinaListScreen(
                onNova = { navController.navigate(Rotas.rotinaForm()) },
                onEditar = { id -> navController.navigate(Rotas.rotinaForm(id)) },
                onVoltar = voltar
            )
        }
        composable(Rotas.ROTINA_FORM, arguments = argumentoId) { entrada ->
            RotinaFormScreen(
                id = entrada.arguments?.getLong("id") ?: -1L,
                onVoltar = voltar
            )
        }

        composable(Rotas.TREINOS) {
            TreinoListScreen(
                onNovo = { navController.navigate(Rotas.treinoForm()) },
                onEditar = { id -> navController.navigate(Rotas.treinoForm(id)) },
                onVoltar = voltar
            )
        }
        composable(Rotas.TREINO_FORM, arguments = argumentoId) { entrada ->
            TreinoFormScreen(
                id = entrada.arguments?.getLong("id") ?: -1L,
                onVoltar = voltar
            )
        }

        composable(Rotas.SERIES) {
            SerieListScreen(
                onNova = { navController.navigate(Rotas.serieForm()) },
                onEditar = { id -> navController.navigate(Rotas.serieForm(id)) },
                onVoltar = voltar
            )
        }
        composable(Rotas.SERIE_FORM, arguments = argumentoId) { entrada ->
            SerieFormScreen(
                id = entrada.arguments?.getLong("id") ?: -1L,
                onVoltar = voltar
            )
        }

        composable(Rotas.METAS) {
            MetaListScreen(
                onNova = { navController.navigate(Rotas.metaForm()) },
                onEditar = { id -> navController.navigate(Rotas.metaForm(id)) },
                onVoltar = voltar
            )
        }
        composable(Rotas.META_FORM, arguments = argumentoId) { entrada ->
            MetaFormScreen(
                id = entrada.arguments?.getLong("id") ?: -1L,
                onVoltar = voltar
            )
        }
    }
}
