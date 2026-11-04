package com.equipe.treinoapp.ui.exercicio

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
import com.equipe.treinoapp.data.model.Exercicio
import com.equipe.treinoapp.ui.componentes.BarraTopo
import com.equipe.treinoapp.ui.componentes.SeletorDropdown
import com.equipe.treinoapp.util.GRUPOS_MUSCULARES
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercicioFormScreen(
    id: Long,
    onVoltar: () -> Unit,
    viewModel: ExercicioViewModel = hiltViewModel()
) {
    val novo = id == -1L

    var nome by rememberSaveable { mutableStateOf("") }
    var grupo by rememberSaveable { mutableStateOf(GRUPOS_MUSCULARES.first()) }
    var equipamento by rememberSaveable { mutableStateOf("") }
    var carregado by rememberSaveable { mutableStateOf(novo) }
    var erroNome by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (!carregado) {
            viewModel.buscarPorId(id)?.let {
                nome = it.nome
                grupo = it.grupoMuscular
                equipamento = it.equipamento
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo(if (novo) "Novo exercício" else "Editar exercício", onVoltar) }
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
                label = { Text("Nome do exercício") },
                isError = erroNome,
                supportingText = { if (erroNome) Text("Informe o nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SeletorDropdown(
                rotulo = "Grupo muscular",
                opcoes = GRUPOS_MUSCULARES,
                selecionado = grupo,
                textoDe = { it },
                onSelecionar = { grupo = it }
            )

            OutlinedTextField(
                value = equipamento,
                onValueChange = { equipamento = it },
                label = { Text("Equipamento (ex.: Barra, Halteres)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nome.isBlank()) {
                        erroNome = true
                    } else {
                        scope.launch {
                            viewModel.salvar(
                                Exercicio(
                                    id = if (novo) 0 else id,
                                    nome = nome.trim(),
                                    grupoMuscular = grupo,
                                    equipamento = equipamento.trim()
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
