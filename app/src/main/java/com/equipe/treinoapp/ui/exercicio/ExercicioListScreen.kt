package com.equipe.treinoapp.ui.exercicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.equipe.treinoapp.data.model.Exercicio
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.ConfirmarExclusaoDialog
import com.equipe.treinoapp.ui.componentes.EstadoVazio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercicioListScreen(
    onNovo: () -> Unit,
    onEditar: (Long) -> Unit,
    onVoltar: () -> Unit,
    viewModel: ExercicioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var exercicioParaExcluir by remember { mutableStateOf<Exercicio?>(null) }

    Scaffold(
        topBar = { BarraTopo("Exercícios", onVoltar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovo) {
                Icon(Icons.Default.Add, contentDescription = "Novo exercício")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.busca,
                onValueChange = viewModel::onBuscaChange,
                label = { Text("Buscar exercício") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.exercicios.isEmpty()) {
                EstadoVazio("Nenhum exercício encontrado")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(uiState.exercicios, key = { it.id }) { exercicio ->
                        Card(
                            onClick = { onEditar(exercicio.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(exercicio.nome, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${exercicio.grupoMuscular} • ${exercicio.equipamento}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                IconButton(onClick = { exercicioParaExcluir = exercicio }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    exercicioParaExcluir?.let { exercicio ->
        ConfirmarExclusaoDialog(
            mensagem = "Excluir \"${exercicio.nome}\"? As séries registradas com ele também serão excluídas.",
            onConfirmar = {
                viewModel.excluir(exercicio)
                exercicioParaExcluir = null
            },
            onCancelar = { exercicioParaExcluir = null }
        )
    }
}
