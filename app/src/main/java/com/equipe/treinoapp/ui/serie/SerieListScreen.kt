package com.equipe.treinoapp.ui.serie

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
import androidx.compose.material3.CardDefaults
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
import com.equipe.treinoapp.data.model.SerieComExercicio
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.ConfirmarExclusaoDialog
import com.equipe.treinoapp.ui.componentes.EstadoVazio
import com.equipe.treinoapp.util.formatarData
import com.equipe.treinoapp.util.formatarPeso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieListScreen(
    onNova: () -> Unit,
    onEditar: (Long) -> Unit,
    onVoltar: () -> Unit,
    viewModel: SerieViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var serieParaExcluir by remember { mutableStateOf<SerieComExercicio?>(null) }

    Scaffold(
        topBar = { BarraTopo("Histórico de séries", onVoltar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNova) {
                Icon(Icons.Default.Add, contentDescription = "Nova série")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Volume total", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "${formatarPeso(uiState.volumeTotalKg)} kg",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "${uiState.series.size} séries registradas",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (uiState.series.isEmpty()) {
                EstadoVazio(
                    texto = "Nenhuma série registrada. Toque em + para registrar.",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.series, key = { it.serie.id }) { item ->
                        Card(
                            onClick = { onEditar(item.serie.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.exercicioNome, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${formatarPeso(item.serie.peso)} kg × ${item.serie.repeticoes} reps",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        formatarData(item.serie.dataMillis),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { serieParaExcluir = item }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    serieParaExcluir?.let { item ->
        ConfirmarExclusaoDialog(
            mensagem = "Excluir a série de ${item.exercicioNome} (${formatarData(item.serie.dataMillis)})?",
            onConfirmar = {
                viewModel.excluir(item.serie)
                serieParaExcluir = null
            },
            onCancelar = { serieParaExcluir = null }
        )
    }
}
