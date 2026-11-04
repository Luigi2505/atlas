package com.equipe.treinoapp.ui.rotina

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.equipe.treinoapp.data.model.Rotina
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.DIAS_SEMANA
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotinaFormScreen(
    id: Long,
    onVoltar: () -> Unit,
    viewModel: RotinaViewModel = hiltViewModel()
) {
    val nova = id == -1L

    var nome by rememberSaveable { mutableStateOf("") }
    var diaSemana by rememberSaveable { mutableStateOf(DIAS_SEMANA.first()) }
    var observacoes by rememberSaveable { mutableStateOf("") }
    var carregado by rememberSaveable { mutableStateOf(nova) }
    var erroNome by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (!carregado) {
            viewModel.buscarPorId(id)?.let {
                nome = it.nome
                diaSemana = it.diaSemana
                observacoes = it.observacoes
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo(if (nova) "Nova rotina" else "Editar rotina", onVoltar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = {
                    nome = it
                    erroNome = false
                },
                label = { Text("Nome da rotina (ex.: Push Day)") },
                isError = erroNome,
                supportingText = { if (erroNome) Text("Informe o nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SeletorDropdown(
                rotulo = "Dia da semana",
                opcoes = DIAS_SEMANA,
                selecionado = diaSemana,
                textoDe = { it },
                onSelecionar = { diaSemana = it }
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
                    if (nome.isBlank()) {
                        erroNome = true
                    } else {
                        scope.launch {
                            viewModel.salvar(
                                Rotina(
                                    id = if (nova) 0 else id,
                                    nome = nome.trim(),
                                    diaSemana = diaSemana,
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
