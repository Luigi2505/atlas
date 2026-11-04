package com.equipe.treinoapp.ui.meta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.equipe.treinoapp.data.model.Meta
import com.equipe.treinoapp.ui.componentes.BarraTopo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetaFormScreen(
    id: Long,
    onVoltar: () -> Unit,
    viewModel: MetaViewModel = hiltViewModel()
) {
    val nova = id == -1L

    var titulo by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var concluida by rememberSaveable { mutableStateOf(false) }
    var carregado by rememberSaveable { mutableStateOf(nova) }
    var erroTitulo by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        if (!carregado) {
            viewModel.buscarPorId(id)?.let {
                titulo = it.titulo
                descricao = it.descricao
                concluida = it.concluida
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = { BarraTopo(if (nova) "Nova meta" else "Editar meta", onVoltar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    erroTitulo = false
                },
                label = { Text("Título da meta") },
                isError = erroTitulo,
                supportingText = { if (erroTitulo) Text("Informe o título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = concluida, onCheckedChange = { concluida = it })
                Text("Meta concluída")
            }

            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        erroTitulo = true
                    } else {
                        scope.launch {
                            viewModel.salvar(
                                Meta(
                                    id = if (nova) 0 else id,
                                    titulo = titulo.trim(),
                                    descricao = descricao.trim(),
                                    concluida = concluida
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
