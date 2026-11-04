package com.equipe.treinoapp.ui.meta

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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.data.model.Meta
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.ConfirmarExclusaoDialog
import com.equipe.treinoapp.ui.componentes.EstadoVazio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetaListScreen(
    onNova: () -> Unit,
    onEditar: (Long) -> Unit,
    onVoltar: () -> Unit,
    viewModel: MetaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var metaParaExcluir by remember { mutableStateOf<Meta?>(null) }

    Scaffold(
        topBar = { BarraTopo("Metas", onVoltar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNova) {
                Icon(Icons.Default.Add, contentDescription = "Nova meta")
            }
        }
    ) { padding ->
        if (uiState.metas.isEmpty()) {
            Column(modifier = Modifier.padding(padding)) {
                EstadoVazio("Nenhuma meta cadastrada. Toque em + para criar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.metas, key = { it.id }) { meta ->
                    Card(
                        onClick = { onEditar(meta.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = meta.concluida,
                                onCheckedChange = { viewModel.alternarConcluida(meta) }
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = meta.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    textDecoration = if (meta.concluida) TextDecoration.LineThrough else null
                                )
                                if (meta.descricao.isNotBlank()) {
                                    Text(
                                        text = meta.descricao,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = { metaParaExcluir = meta }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }

    metaParaExcluir?.let { meta ->
        ConfirmarExclusaoDialog(
            mensagem = "Excluir a meta \"${meta.titulo}\"?",
            onConfirmar = {
                viewModel.excluir(meta)
                metaParaExcluir = null
            },
            onCancelar = { metaParaExcluir = null }
        )
    }
}
