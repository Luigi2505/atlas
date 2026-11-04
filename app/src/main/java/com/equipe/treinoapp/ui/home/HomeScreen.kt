package com.equipe.treinoapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.util.NOME_ACADEMIA

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAbrirExercicios: () -> Unit,
    onAbrirRotinas: () -> Unit,
    onAbrirTreinos: () -> Unit,
    onAbrirSeries: () -> Unit,
    onAbrirMetas: () -> Unit,
    onAbrirPerfil: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BarraTopo(
                titulo = NOME_ACADEMIA,
                acoes = {
                    IconButton(onClick = onAbrirPerfil) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Meu perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.nomeUsuario.isNotBlank()) {
                Text(
                    text = "Olá, ${uiState.nomeUsuario}!",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumo", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.size(8.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ItemResumo(uiState.exercicios, "Exercícios", Modifier.weight(1f))
                        ItemResumo(uiState.rotinas, "Rotinas", Modifier.weight(1f))
                        ItemResumo(uiState.treinos, "Treinos", Modifier.weight(1f))
                        ItemResumo(uiState.series, "Séries", Modifier.weight(1f))
                        ItemResumo(uiState.metasPendentes, "Metas", Modifier.weight(1f))
                    }
                }
            }

            Text("Cadastros", style = MaterialTheme.typography.titleMedium)

            CartaoModulo(
                titulo = "Exercícios",
                descricao = "Biblioteca de exercícios por grupo muscular",
                icone = Icons.AutoMirrored.Filled.List,
                onClick = onAbrirExercicios
            )
            CartaoModulo(
                titulo = "Rotinas",
                descricao = "Monte seus treinos da semana (Push, Pull, Legs...)",
                icone = Icons.Default.DateRange,
                onClick = onAbrirRotinas
            )
            CartaoModulo(
                titulo = "Treinos",
                descricao = "Registre os treinos que você realizou",
                icone = Icons.Default.PlayArrow,
                onClick = onAbrirTreinos
            )
            CartaoModulo(
                titulo = "Histórico de séries",
                descricao = "Peso e repetições por exercício, com volume total",
                icone = Icons.Default.CheckCircle,
                onClick = onAbrirSeries
            )
            CartaoModulo(
                titulo = "Metas",
                descricao = "Acompanhe seus objetivos de treino",
                icone = Icons.Default.Star,
                onClick = onAbrirMetas
            )
        }
    }
}

@Composable
private fun ItemResumo(valor: Int, rotulo: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor.toString(), style = MaterialTheme.typography.headlineSmall)
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CartaoModulo(
    titulo: String,
    descricao: String,
    icone: ImageVector,
    onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(
                    descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
