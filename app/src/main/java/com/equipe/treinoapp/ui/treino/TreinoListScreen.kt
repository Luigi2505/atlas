package com.equipe.treinoapp.ui.treino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.data.model.TreinoComRotina
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.ConfirmarExclusaoDialog
import com.equipe.treinoapp.ui.componentes.EstadoVazio
import com.equipe.treinoapp.util.formatarData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoListScreen(
    onNovo: () -> Unit,
    onEditar: (Long) -> Unit,
    onVoltar: () -> Unit,
    viewModel: TreinoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var treinoParaExcluir by remember { mutableStateOf<TreinoComRotina?>(null) }

    Scaffold(
        topBar = { BarraTopo("Treinos", onVoltar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovo) {
                Icon(Icons.Default.Add, contentDescription = "Novo treino")
            }
        }
    ) { padding ->
        if (uiState.treinos.isEmpty()) {
            Column(modifier = Modifier.padding(padding)) {
                EstadoVazio("Nenhum treino registrado. Toque em + para registrar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.treinos, key = { it.treino.id }) { item ->
                    Card(
                        onClick = { onEditar(item.treino.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.rotinaNome ?: "Treino sem rotina",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${formatarData(item.treino.dataMillis)} • ${item.treino.duracaoMinutos} min",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (item.treino.observacoes.isNotBlank()) {
                                    Text(
                                        text = item.treino.observacoes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = { treinoParaExcluir = item }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }

    treinoParaExcluir?.let { item ->
        ConfirmarExclusaoDialog(
            mensagem = "Excluir o treino de ${formatarData(item.treino.dataMillis)}?",
            onConfirmar = {
                viewModel.excluir(item.treino)
                treinoParaExcluir = null
            },
            onCancelar = { treinoParaExcluir = null }
        )
    }
}
