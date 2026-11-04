package com.equipe.treinoapp.ui.treino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipe.treinoapp.data.model.Treino
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorData
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.hojeMillis
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoFormScreen(
    id: Long,
    onVoltar: () -> Unit,
    viewModel: TreinoViewModel = hiltViewModel()
) {
    val novo = id == -1L
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var rotinaId by rememberSaveable { mutableStateOf<Long?>(null) }
    var dataMillis by rememberSaveable { mutableStateOf(hojeMillis()) }
    var duracao by rememberSaveable { mutableStateOf("") }
    var observacoes by rememberSaveable { mutableStateOf("") }
    var carregado by rememberSaveable { mutableStateOf(novo) }
    var erroDuracao by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (!carregado) {
            viewModel.buscarPorId(id)?.let {
                rotinaId = it.rotinaId
                dataMillis = it.dataMillis
                duracao = it.duracaoMinutos.toString()
                observacoes = it.observacoes
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo(if (novo) "Novo treino" else "Editar treino", onVoltar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SeletorDropdown(
                rotulo = "Rotina",
                opcoes = uiState.rotinas,
                selecionado = uiState.rotinas.firstOrNull { it.id == rotinaId },
                textoDe = { "${it.nome} (${it.diaSemana})" },
                onSelecionar = { rotinaId = it.id }
            )
            if (rotinaId != null) {
                TextButton(onClick = { rotinaId = null }) { Text("Remover rotina") }
            }

            SeletorData(
                dataMillis = dataMillis,
                onDataSelecionada = { dataMillis = it }
            )

            OutlinedTextField(
                value = duracao,
                onValueChange = {
                    duracao = it.filter { c -> c.isDigit() }
                    erroDuracao = false
                },
                label = { Text("Duração (minutos)") },
                isError = erroDuracao,
                supportingText = { if (erroDuracao) Text("Informe a duração em minutos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                label = { Text("Observações") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val minutos = duracao.toIntOrNull()
                    if (minutos == null || minutos <= 0) {
                        erroDuracao = true
                    } else {
                        scope.launch {
                            viewModel.salvar(
                                Treino(
                                    id = if (novo) 0 else id,
                                    rotinaId = rotinaId,
                                    dataMillis = dataMillis,
                                    duracaoMinutos = minutos,
                                    observacoes = observacoes.trim()
                                )
                            )
                            onVoltar()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}
