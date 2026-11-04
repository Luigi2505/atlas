package com.equipe.treinoapp.ui.rotina

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
import com.equipe.treinoapp.data.model.Rotina
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.ConfirmarExclusaoDialog
import com.equipe.treinoapp.ui.componentes.EstadoVazio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotinaListScreen(
    onNova: () -> Unit,
    onEditar: (Long) -> Unit,
    onVoltar: () -> Unit,
    viewModel: RotinaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var rotinaParaExcluir by remember { mutableStateOf<Rotina?>(null) }

    Scaffold(
        topBar = { BarraTopo("Rotinas", onVoltar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNova) {
                Icon(Icons.Default.Add, contentDescription = "Nova rotina")
            }
        }
    ) { padding ->
        if (uiState.rotinas.isEmpty()) {
            Column(modifier = Modifier.padding(padding)) {
                EstadoVazio("Nenhuma rotina cadastrada. Toque em + para criar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.rotinas, key = { it.id }) { rotina ->
                    Card(
                        onClick = { onEditar(rotina.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(rotina.nome, style = MaterialTheme.typography.titleMedium)
                                Text(rotina.diaSemana, style = MaterialTheme.typography.bodyMedium)
                                if (rotina.observacoes.isNotBlank()) {
                                    Text(
                                        rotina.observacoes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = { rotinaParaExcluir = rotina }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }

    rotinaParaExcluir?.let { rotina ->
        ConfirmarExclusaoDialog(
            mensagem = "Excluir a rotina \"${rotina.nome}\"? Os treinos já registrados são mantidos.",
            onConfirmar = {
                viewModel.excluir(rotina)
                rotinaParaExcluir = null
            },
            onCancelar = { rotinaParaExcluir = null }
        )
    }
}
